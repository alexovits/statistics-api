package com.alexovits.stats.model;

/**
 * Created by zsoltszabo on 8/8/17.
 */
public class Statistics {
    private double sum, avg, max, min;
    private int count;

    private Statistics() {
        sum = 0;
        avg = 0;
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        count = 0;
    }

    public Statistics(double sum, double avg, double max, double min, int count){
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public double getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Statistics={sum:"+sum+", avg:"+avg+", max:"+max+", min:"+min+", count:"+count+"}";
    }
}
