package com.alexovits.stats.test;

import com.alexovits.stats.api.exception.InvalidTransactionException;
import com.alexovits.stats.api.service.StatisticsService;
import com.alexovits.stats.api.service.impl.StatisticsServiceBean;
import com.alexovits.stats.model.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.Instant;

/**
 * Created by zsoltszabo on 8/9/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class StatisticsServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(StatisticsServiceTest.class);

    private StatisticsService victimStatisticsService;

    @Before
    public void setUp() {
        victimStatisticsService = new StatisticsServiceBean();
    }

    @Test
    public void testInsertTransactionSuccess() {
        Exception exception = null;
        try {
            victimStatisticsService.insertTransaction(new Transaction(0, Instant.now()));
        } catch (InvalidTransactionException e) {
            exception = e;
        }
        Assert.assertNull("Failure - Did not expect InvalidTransactionException", exception);
    }

    @Test
    public void testInsertTransactionFailure() {
        Exception exception = null;
        try {
            victimStatisticsService.insertTransaction(new Transaction(0, Instant.now().minusSeconds(61)));
        } catch (InvalidTransactionException e) {
            exception = e;
        }
        Assert.assertNotNull("Failure - Expected InvalidTransactionException", exception);
    }

}



