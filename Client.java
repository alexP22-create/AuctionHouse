package com.company;

public class Client{
    private int id;
    private final String name;
    private String adress;
    private int nrParticipations;
    private int nrWonAuctions;
    //field which indicates the buget at the start of the auction
    private double buget;
    //field which indicates the money available to auction
    private double maxPrice;
    //field which indicates if a broker was assigned to this client during auction
    private Broker assignedBroker;
    //the wanted product
    private Product product;
    //the minimum sum of money the client has to invest to win the auction
    private double lastMaxAuctionedPrice;

    public Client(String name, double buget){
        this.name = name;
        this.buget = buget;
    }

    public String getName() {
        return name;
    }

    public Product getProduct() {
        return product;
    }

    // return the money auctioned by the client by a chosen algorithm
    public double getAuctionedMoney() {
        if(lastMaxAuctionedPrice < maxPrice) {
            //returns a random value betwen the max limit and the min limit
            return lastMaxAuctionedPrice + (maxPrice - lastMaxAuctionedPrice) * Math.random();
        }
        return maxPrice;
    }

    public double getLastMaxAuctionedPrice() {
        return lastMaxAuctionedPrice;
    }

    public void informClient(double lastMaxAuctionedPrice){
        this.lastMaxAuctionedPrice = lastMaxAuctionedPrice;
    }
    public Broker getAssignedBroker() {
        return assignedBroker;
    }

    public int getNrWonAuctions() {
        return nrWonAuctions;
    }

    public int getNrParticipations() {
        return nrParticipations;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAssignedBroker(Broker broker){
        assignedBroker = broker;
    }

    //next method will return the max price auctioned, by extracting the Broker's comision
    public void setMaxAuctionedMoney(){
        double price = 0.0;
        if(this instanceof LegalPerson){
            if(nrParticipations > 25)
                price = buget - buget / 10;
            else
                price = buget- buget / 4;
        }
        if(this instanceof PhysicalPerson){
            if(nrParticipations > 5)
                price = buget - buget * 15 / 100;
            else
                price = buget - buget / 5;
        }
        maxPrice = price;
    }
    //the client is requesting his wanted product
    public void requestProduct(AuctionHouse ah, int idProduct) {
        setMaxAuctionedMoney();
        //find the index of the wanted product if the product exists
        Product wantedProduct = ah.getRequestedProduct(idProduct);
        //we will set the wantedProduct field from Client class
        this.setProduct(wantedProduct);
        //the auction house will give the client a random broker
        ah.setBroker(this);
    }

    public void increaseParticipations(){
        nrParticipations++;
    }
    public void increaseNrWonAuctions(){
        nrWonAuctions++;
    }
    public void takeMoney(){
        buget = buget - lastMaxAuctionedPrice;
    }
}
