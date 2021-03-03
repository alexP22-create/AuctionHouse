package com.company;

import java.util.ArrayList;

public class Broker extends Employee{
    private final ArrayList<Client> clients;
    private final AuctionHouse auctionHouse;
    //field for strategy design pattern
    private final StrategyContext strategyComission;
    //private double lastMaxAuctionedPrice;

    public void addClient(Client client){
        clients.add(client);
    }

    public Broker(AuctionHouse auctionHouse) {
        strategyComission = new StrategyContext();
        clients = new ArrayList<>();
        this.auctionHouse = auctionHouse;
    }

    public ArrayList<Client> getClients() {
        return clients;
    }


    //return the money the client is auctioning applying the commission
    public double getAuctionedSum(Client client){
        double clientMoney = client.getAuctionedMoney();
        if(client instanceof LegalPerson){
            strategyComission.setStrategy(new LegalComissionStrategy());
            return strategyComission.pay(clientMoney, client.getNrParticipations());
        }
        if(client instanceof PhysicalPerson){
            strategyComission.setStrategy(new PhysicalComissionStrategy());
            return strategyComission.pay(clientMoney, client.getNrParticipations());
        }
        return clientMoney;
    }

    //clear all the connections between clients and their brokers
    public void clearOldClients(int idProduct){
        boolean allRemoved = false;
        while(!allRemoved) {
            Client exClient = null;
            for (Client client : clients) {
                if (client.getProduct().getId() == idProduct) {
                    exClient = client;
                }
            }
            if (exClient != null) {
                clients.remove(exClient);
                allRemoved = false;
            }
            else {
                allRemoved = true;
            }
        }
    }
    //inform the clients about the min amount of money they have to auction
    public void informBroker(double lastMaxAuctionedPrice, int idProduct){
        for(Client client : clients){
            if(idProduct == client.getProduct().getId()) {
                double clientPrice = 0.0;
                //now we will have to keep in mind the commission
                if (client instanceof LegalPerson) {
                    strategyComission.setStrategy(new LegalComissionStrategy());
                    clientPrice = strategyComission.inform(lastMaxAuctionedPrice, client.getNrParticipations());
                }
                if (client instanceof PhysicalPerson) {
                   strategyComission.setStrategy(new PhysicalComissionStrategy());
                   clientPrice = strategyComission.inform(lastMaxAuctionedPrice, client.getNrParticipations());
                }
                client.informClient(clientPrice);
            }
        }
    }
    //eliminate all the sold products
    public void modifyProductList() {
        auctionHouse.lock.lock();
        try {
            //while the product list is empty wait until
            while(auctionHouse.getProducts().size() == 0)
                auctionHouse.NotEmptyList.await();
            for (int i = 0; i < auctionHouse.getProducts().size(); ) {
                //eliminate the sold products
                if (auctionHouse.getProducts().get(i).getSellingPrice() != 0.0) {
                    System.out.println("S-a eliminat produsul cu id-ul " + auctionHouse.getProducts().get(i).getId());
                    auctionHouse.getProducts().remove(i);
                    auctionHouse.EmptyList.signal();
                }
                else
                    i ++;
            }
        }
        catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        finally {
            auctionHouse.lock.unlock();
        }
    }
    public void notifyEndAuction(Client client){
        client.increaseParticipations();
    }
    public void notifyWinner(Client client){
        client.takeMoney();
        client.increaseNrWonAuctions();
    }
    @Override
    public void run() {
        modifyProductList();
    }
}
