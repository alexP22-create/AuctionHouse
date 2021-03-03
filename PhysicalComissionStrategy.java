package com.company;

public class PhysicalComissionStrategy implements Commission{
    //the methods used for working with a commission in the case of a physical person
    @Override
    public double valueAfterCommission(double price, int participations) {
        if(participations > 5)
            return price - price * 15 / 100;
        else
            return price - price / 5;
    }

    @Override
    public double valueBeforeCommission(double price, int participations) {
        if (participations > 5)
            return price * 100 / 85;
        else
            return price * 100 / 80;
    }
}
