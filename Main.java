package com.company;
//POSTOLACHE Alexandru-Gabriel 321CB
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        //create the auction house with the Singleton
        AuctionHouse auctionHouse = AuctionHouse.Instance();
        //create the administrator
        Administrator administrator = new Administrator(auctionHouse);
        //multithreading
        ArrayList<Broker> brokers = new ArrayList<>(3);
        Broker broker1 = new Broker(auctionHouse);
        Broker broker2 = new Broker(auctionHouse);
        Broker broker3 = new Broker(auctionHouse);
        brokers.add(broker1);
        brokers.add(broker2);
        brokers.add(broker3);
        auctionHouse.setBrokers(brokers);
        auctionHouse.setAdministrator(administrator);
        //start the multithreading
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(administrator);
        executor.execute(auctionHouse);
        executor.execute(broker1);
        executor.shutdown();

    }
}
