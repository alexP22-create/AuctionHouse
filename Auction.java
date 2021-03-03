package com.company;

import java.util.ArrayList;

public class Auction {
    private int id;
    private final int nrParticipants;
    private final int idProduct;
    private final int maxNrSteps;
    //administrator
    private Administrator administrator;
    //nr of signed participants
    private int signedParticipants;
    // auction house
    private final AuctionHouse auctionHouse;
    // brokers used in this Auction
    private final ArrayList<Broker> brokersUsed;

    public Auction(int idProduct, int nrParticipants, int maxNrSteps, AuctionHouse auctionHouse) {
        this.idProduct = idProduct;
        this.nrParticipants = nrParticipants;
        this.maxNrSteps = maxNrSteps;
        this.auctionHouse = auctionHouse;
        this.brokersUsed = new ArrayList<>();
    }

    public void increaseParticipants(){
        this.signedParticipants++;
    }
    public int getIdProduct() {
        return idProduct;
    }

    public boolean startable(){
        //we need to get the nr of particiapants
        return nrParticipants == signedParticipants;
    }
    public void addAuctionBroker(Broker broker){
        brokersUsed.add(broker);
    }

    //start the auction
    public void start(){
        Client winner = null;
        int step = 1;
        System.out.println("Licitatia pentru produsul cu id-ul: " + idProduct);
        while(step <= maxNrSteps) {
            System.out.println("\tPasul " + step);
            //for every step of the auction find the winner
            winner = auctionHouse.maxSumWinner(brokersUsed, this.getIdProduct());
            step++;
        }
        //final winner
        if(winner != null){
            if(winner.getLastMaxAuctionedPrice() >= winner.getProduct().getMinPrice()) {
                //the auction is finnished and the winner is decided
                System.out.println("======== The winner for the product " + this.getIdProduct()+" is: " + winner.getName());
                winner.getAssignedBroker().notifyWinner(winner);
            }
            else{
                System.out.println("========= The product " + this.getIdProduct() + " didn't sell =========");
            }
            //notify the clients about end of the auction
            for (Broker broker : brokersUsed) {
                for (Client client : broker.getClients()) {
                    if(client.getProduct().getId() == idProduct) {
                        broker.notifyEndAuction(client);
                    }
                }
                //clear the communication between brokers and clients
                broker.clearOldClients(idProduct);
            }
        }
    }
}
