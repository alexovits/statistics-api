package com.alexovits.stats.api.service.impl;

import com.alexovits.stats.api.exception.InvalidTransactionException;
import com.alexovits.stats.api.service.StatisticsService;
import com.alexovits.stats.model.Statistics;
import com.alexovits.stats.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Created by zsoltszabo on 8/8/17.
 */
@Service
public class StatisticsServiceBean implements StatisticsService{

    private static final Logger LOG = LoggerFactory.getLogger(StatisticsServiceBean.class);

    private List<Transaction> latestTransactions;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private double transactionSum;
    private double maxTransaction;
    private double minTransaction;

    public StatisticsServiceBean(){

    }

    /** Initializes attributes of the class and starts a daemon thread that will repeat every second **/
    @PostConstruct
    public void init(){
        transactionSum = 0;
        maxTransaction = Integer.MIN_VALUE;
        minTransaction = Integer.MAX_VALUE;
        latestTransactions = new ArrayList<>();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // Using scheduleWithFixedDelay because it counts delay from the end of the thread
        ScheduledFuture daemonTask = scheduledExecutorService.scheduleWithFixedDelay(
                () -> analyzeTransactions(),
                0,
                1000,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Inserts a new transaction into the collection.
     * If the transaction is already expired InvalidTransactionException gets thrown.
     * @param transaction Transaction object containing a new transaction from a client
     * @throws InvalidTransactionException
     */
    @Override
    public void insertTransaction(Transaction transaction) throws InvalidTransactionException {
        // Read lock needed because this method should be blocked whenever getStats or analyzeTransactions is being executed.
        lock.readLock().lock();
        try {
            if(checkExpiredTimestamp(transaction.getTimestamp())) {
                throw new InvalidTransactionException("Timestamp is expired!", transaction.getTimestamp());
            }
            latestTransactions.add(transaction);
            LOG.info("Inserted new transaction: " + transaction);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Creates a Statistics object from the current data
     * @return Statistics with the current informations
     */
    @Override
    public Statistics getStats() {
        // Write lock needed since the daemon thread could change the values while constructing the response
        lock.writeLock().lock();
        try {
            int transactionsCount = latestTransactions.size(); // O(1) constant time since the ArrayList implementation stores it dynamically
            double transactionsAvg = (transactionsCount == 0) ? 0 : transactionSum/transactionsCount; // Avoid division by zero
            Statistics stats = new Statistics(transactionSum, transactionsAvg, maxTransaction, minTransaction, transactionsCount);
            LOG.info("Response for statistics: " + stats);
            return new Statistics(transactionSum, transactionsAvg, maxTransaction, minTransaction, transactionsCount);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * This method gets called every second.
     * As a daemon thread this methods filters out all the old transactions and updates the current statistics.
     * The reason for the statistics being recalculated is because accessing these information should be O(1)
     * and if the getStats() would have to iterate the collection every time it would make it O(n).
     */
    private void analyzeTransactions() {
        // Write lock needed since the insertion method's would interfere with the calculations. They are put on hold for the time being.
        lock.writeLock().lock();
        try {
            // Filter out the transactions that are older than one second
            latestTransactions = latestTransactions
                    .stream()
                    .filter(tr -> !checkExpiredTimestamp(tr.getTimestamp()))
                    .collect(Collectors.toList());
            transactionSum = 0;
            maxTransaction = Integer.MIN_VALUE;
            minTransaction = Integer.MAX_VALUE;
            latestTransactions.forEach(tr -> {
                transactionSum += tr.getAmount();
                // Search for the greatest element
                if (maxTransaction < tr.getAmount()) {
                    maxTransaction = tr.getAmount();
                }
                // Search for the smallest element
                if (minTransaction > tr.getAmount()) {
                    minTransaction = tr.getAmount();
                }
            });
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Checks whether a timestamp is older tha 1 minute
     * @param timestamp the timestamp to check the condition against
     * @return true if the timestamp is expired, false otherwise
     */
    private boolean checkExpiredTimestamp(Instant timestamp) {
        return Instant.now().minusSeconds(60).isAfter(timestamp) ? true : false;
    }
}
