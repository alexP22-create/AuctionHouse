package com.company;
//class used to implement the strategy design pattern
public class StrategyContext {
    private Commission strategy;
    public void setStrategy(Commission strategy) {
        this.strategy = strategy;
    }
    //based on the strategy the next 2 functions will behave differently
    public double pay(double price, int nr){
        return strategy.valueAfterCommission(price, nr);
    }
    public double inform(double price, int nr){
        return strategy.valueBeforeCommission(price, nr);
    }
}
