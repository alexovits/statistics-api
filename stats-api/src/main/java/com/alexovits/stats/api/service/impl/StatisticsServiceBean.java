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
    private int transactionSum;
    private int maxTransaction;
    private int minTransaction;

    public StatisticsServiceBean(){

    }

    @PostConstruct
    public void init(){
        transactionSum = 0;
        latestTransactions = new ArrayList<>();
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        // Using scheduleWithFixedDelay because it counts delay from the end of the thread
        ScheduledFuture daemonTask = scheduledExecutorService.scheduleWithFixedDelay(
                () -> {
                    lock.writeLock().lock();
                    try {
//                        String owner = "(*) Daemon---Thread (*)";
//                        System.out.println("*** " + owner + " ***");
                        Instant currentTimeStamp = Instant.now().minusSeconds(1); // The timestamp of a second before
                        // Filter out the transactions that are older than one second
                        latestTransactions = latestTransactions.stream().filter(transaction -> transaction.getTimestamp().isBefore(currentTimeStamp)).collect(Collectors.toList());
                    } finally {
                        lock.writeLock().unlock();
                    }
                },
                0,
                1000,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public void insertTransaction(Transaction transaction) {
        lock.readLock().lock();
        try {
            if(checkExpiredTimestamp(transaction.getTimestamp())) {
                throw new InvalidTransactionException("Timestamp is expired!", transaction.getTimestamp());
            }
            latestTransactions.add(transaction);
            System.out.println(latestTransactions);
        } finally {
            lock.readLock().unlock();
            LOG.info("Kileptem kesz geci");
        }
    }

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

    private boolean checkExpiredTimestamp(Instant timestamp) {
        return Instant.now().minusSeconds(1).isAfter(timestamp) ? true : false;
    }
}
