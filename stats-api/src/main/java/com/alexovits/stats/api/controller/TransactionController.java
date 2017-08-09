package com.alexovits.stats.api.controller;

import com.alexovits.stats.api.service.StatisticsService;
import com.alexovits.stats.model.Statistics;
import com.alexovits.stats.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by zsoltszabo on 8/8/17.
 */

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private StatisticsService statiscticsService;

    @Autowired
    public TransactionController(StatisticsService statiscticsService){
        this.statiscticsService = statiscticsService;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistics> getStatistics() {
        return new ResponseEntity<>(statiscticsService.getStats(), HttpStatus.OK);
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity postTransaction(@RequestBody Transaction transaction) {
        System.out.println("EEEElo");
        statiscticsService.insertTransaction(transaction);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
