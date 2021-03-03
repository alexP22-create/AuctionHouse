package com.company;
//the interface used in the implementation of the strategy design pattern
public interface Commission {
    double valueAfterCommission(double price, int participations);
    double valueBeforeCommission(double price, int participations);
}
