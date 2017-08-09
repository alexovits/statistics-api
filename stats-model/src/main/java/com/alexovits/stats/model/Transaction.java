package com.alexovits.stats.model;

import java.time.Instant;

/**
 * Created by zsoltszabo on 8/8/17.
 */
public class Transaction {
    private double amount;
    private Instant timestamp;

    public Transaction(){}

    public Transaction(double amount, Instant timestamp){
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "{ amount: "+amount+" timestamp: "+timestamp+"}";
    }
}
