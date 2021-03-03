package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Administrator extends Employee{
    private final AuctionHouse auctionHouse;
    //the id product wanted for the client to request
    private int wantedId;

    public void setWantedId(int wantedId) {
        this.wantedId = wantedId;
    }

    //can only exist 3 unsold products in the auction house in the same time
    private static final int productsLimit = 3;

    public Administrator(AuctionHouse auctionHouse) {
        this.auctionHouse = auctionHouse;
    }

    //adds in the products list all the products read from file
    public void modifyProductList() {
        auctionHouse.lock.lock();
        try {
            FactoryProduct factoryProduct = new FactoryProduct();
            File f = new File("inputProducts.txt");
            Scanner scanner = new Scanner(f);
            while (scanner.hasNextLine()) {
                //while there are too many products wait until a few will be brought
                while (auctionHouse.getProducts().size() >= productsLimit)
                    auctionHouse.EmptyList.await();
                String data = scanner.nextLine();
                Product product = factoryProduct.createProduct(data);
                auctionHouse.getProducts().add(product);
                //the respective auction will be made
                auctionHouse.initializeAuctions();
                System.out.println("Add the product with the id: " + product.getId());
                //when the list of products is full the wanted products will be all there
                //if the wanted product was read signal to continue with the client's requests
                if(product.getId() == wantedId)
                    auctionHouse.ProductNotYet.signal();
            }

            scanner.close();
            //signal that the products list is not empty
            auctionHouse.NotEmptyList.signal();
        }
        catch (FileNotFoundException | InterruptedException ex1){
            System.out.println(ex1.getMessage());
        }
        finally {
            auctionHouse.lock.unlock();
        }
    }

    @Override
    public void run() {
        modifyProductList();
    }

}
