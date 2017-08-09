package com.alexovits.stats.api.service;

import com.alexovits.stats.api.exception.InvalidTransactionException;
import com.alexovits.stats.model.Statistics;
import com.alexovits.stats.model.Transaction;

/**
 * Created by zsoltszabo on 8/8/17.
 */
public interface StatisticsService {
    void insertTransaction(Transaction transaction) throws InvalidTransactionException;
    Statistics getStats();
}
