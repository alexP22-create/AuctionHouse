package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AuctionHouse implements Runnable{
    //singleton design pattern;
    private static AuctionHouse onlyInstance;
    public static AuctionHouse Instance(){
        if(onlyInstance == null)
            onlyInstance = new AuctionHouse();
        return onlyInstance;
    }
    //generic lists
    private final ArrayList<Product> products;
    private final ArrayList<Client> clients;
    private final ArrayList<Auction> auctions;
    //field which contains all the available brokers
    private ArrayList<Broker> brokers;
    //the only Administrator
    private Administrator administrator;
    public AuctionHouse(){
        products = new ArrayList<>();
        clients = new ArrayList<>();
        auctions = new ArrayList<>();
    }

    public void setBrokers(ArrayList<Broker> brokers) {
        this.brokers = brokers;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public void initializeAuctions(){
        //for every product there will be an inactive Auction
        for(Product product : products){
            if(product.getIsAuctioned() == 0) {
                Auction auction = new Auction(product.getId(), 4, 5, this);
                auctions.add(auction);
                product.setIsAuctioned(1);
            }
        }
    }

    //gives the client a random broker
    public void setBroker(Client client){
        Random random = new Random();
        //pick a random broker from the array
        Broker broker = brokers.get(random.nextInt(brokers.size()));
        client.setAssignedBroker(broker);
        broker.addClient(client);
    }
    public ArrayList<Product> getProducts() {
        return products;
    }

    //find the requested product and returns it to the client
    public Product getRequestedProduct(int idProduct){
        for(Product product : products){
            if(product.getId() == idProduct) {
                //increase the number of particiapnts in the respective Auction
                for (Auction auction : auctions) {
                    if (auction.getIdProduct() == idProduct) {
                        auction.increaseParticipants();
                    }
                }
                return product;
            }
        }
        return null;
    }
    //next function will check if the auctions are startable and in that case will start them
    public void checkAuctions(){
        Auction endedAuction = null;
        for(Auction auction : auctions){
            if(auction.startable()){
                //set the brokers used in the Auction
                for(Broker broker : brokers){
                    for(Client client:broker.getClients()){
                        if(client.getProduct().getId() == auction.getIdProduct()) {
                            auction.addAuctionBroker(broker);
                            break;
                        }
                    }
                }
                auction.start();
                //after the auction ended it will be removed
                endedAuction = auction;
            }
        }
        if(endedAuction != null)
            auctions.remove(endedAuction);
    }
    //find the winner of the current auction step
    public Client maxSumWinner(ArrayList<Broker> auctionBrokers, int idProduct){
        double max = 0.0;
        Client winner = null;
        for(Broker broker : auctionBrokers){
            for(Client client : broker.getClients()){
                if(idProduct == client.getProduct().getId()) {
                    double auctionedSum = Math.round(broker.getAuctionedSum(client) * 100.0) / 100.0;
                    System.out.println("\t\tClientul " + client.getName() + " liciteaza: " + auctionedSum);
                    if (auctionedSum == max && winner != null) {
                        if (client.getNrWonAuctions() > winner.getNrWonAuctions()) {
                            max = broker.getAuctionedSum(client);
                            winner = client;
                            //set the selling price until this point
                            winner.getProduct().setSellingPrice(auctionedSum);
                        }
                    }
                    if (auctionedSum > max) {
                        max = auctionedSum;
                        winner = client;
                        //set the selling price until this point
                        winner.getProduct().setSellingPrice(auctionedSum);
                    }
                }
            }
        }
        //inform the brokers about the winner's sum of money auctioned now
        for (Broker broker : auctionBrokers) {
            broker.informBroker(max, idProduct);
        }
        return winner;
    }
    public boolean existProduct(int id){
        for(Product product : products){
            if(product.getId() == id) {
                return true;
            }
        }
        return false;
    }
    public void readClients(){
        lock.lock();
        try {
            FactoryClient factoryClient = new FactoryClient();
            File f = new File("test1.txt");
            Scanner scanner = new Scanner(f);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                Client client = factoryClient.createClient(data);
                clients.add(client);
                //the last word is the id of the wanted product
                String[] cells = data.split(" ");
                int wantedId = Integer.parseInt(cells[cells.length - 1]);
                //send the id of the product which is wanted by the current client to the administrator
                administrator.setWantedId(wantedId);
                //if the product isn't read yet, wait until it's read
                while(!existProduct(wantedId))
                    ProductNotYet.await();
                //we will request the product for every client
                client.requestProduct(this, wantedId);
                //we will check if any auction can start
                this.checkAuctions();
            }
            //the program is ended
            System.exit(0);
        }
        catch (FileNotFoundException | InterruptedException ex1){
            System.out.println(ex1.getMessage());
        }
        finally {
            lock.unlock();
        }
    }

    //the lock used on the arrays
    Lock lock = new ReentrantLock();
    //lock conditions
    Condition EmptyList = lock.newCondition();
    Condition NotEmptyList = lock.newCondition();
    Condition ProductNotYet = lock.newCondition();

    public void run() {
        readClients();
    }
}
