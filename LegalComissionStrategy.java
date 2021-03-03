package com.company;

public class LegalComissionStrategy implements Commission{
    //the methods used for working with a commission in the case of a legal person
     @Override
    public double valueAfterCommission(double price, int participations) {
        if(participations > 25)
            return price - price / 10;
        else
            return price - price / 4;
    }

    @Override
    public double valueBeforeCommission(double price, int participations) {
        if (participations > 25)
            return  price * 100 / 90;
        else
            return price * 100 / 75;
    }
}
