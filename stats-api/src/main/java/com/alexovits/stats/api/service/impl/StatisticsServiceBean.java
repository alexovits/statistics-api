package com.alexovits.stats.api.service.impl;

import com.alexovits.stats.api.service.StatisticsService;
import com.alexovits.stats.model.Statistics;
import com.alexovits.stats.model.Transaction;
import org.springframework.stereotype.Service;

/**
 * Created by zsoltszabo on 8/8/17.
 */
@Service
public class StatisticsServiceBean implements StatisticsService{

    @Override
    public void insertTransaction(Transaction transaction) {

    }

    @Override
    public Statistics getStats() {
        return null;
    }
}
