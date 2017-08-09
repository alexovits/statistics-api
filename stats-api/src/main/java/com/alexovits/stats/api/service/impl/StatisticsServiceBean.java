package com.alexovits.stats.api.service.impl;

import com.alexovits.stats.api.service.StatisticsService;
import com.alexovits.stats.model.Statistics;
import com.alexovits.stats.model.Transaction;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by zsoltszabo on 8/8/17.
 */
@Service
public class StatisticsServiceBean implements StatisticsService{

    List<Transaction> latestTransactions;
    int i = 0;

    public StatisticsServiceBean(){

    }

    @PostConstruct
    public void init(){

    }

    @Override
    public void insertTransaction(Transaction transaction) {
        System.out.println(++i+":"+transaction);
    }

    @Override
    public synchronized Statistics getStats() {
        return null;
    }
}
