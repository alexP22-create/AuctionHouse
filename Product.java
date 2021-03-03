package com.company;

public class Product {
    private int id;
    private String name;
    private double sellingPrice;
    private double minPrice;
    private int year;

    //field which tells if this product belongs to an auction
    private int isAuctioned;

    public int getIsAuctioned() {
        return isAuctioned;
    }

    public void setIsAuctioned(int isAuctioned) {
        this.isAuctioned = isAuctioned;
    }

    public Product() {
    }
    public Product(ProductBuilder pb){
        this.id = pb.id;
        this.name = pb.name;
        this.minPrice = pb.minPrice;
        this.year = pb.year;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public int getId() {
        return id;
    }
    public double getSellingPrice(){
        return sellingPrice;
    }
    public void setSellingPrice(double sellingPrice){
        this.sellingPrice = sellingPrice;
    }
    public String toString(){
        return "" + id + " " + name + " " + sellingPrice + " " + minPrice + " " + year;
    }

    public static class ProductBuilder {
        //necessary attributes
        private int id;
        private String name;
        private double minPrice;
        private int year;
        public ProductBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public ProductBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder setMinPrice(double minPrice) {
            this.minPrice = minPrice;
            return this;
        }

        public ProductBuilder setYear(int year) {
            this.year = year;
            return this;
        }

        public ProductBuilder(){
        }
        public Product buildProduct(){
            return new Product(this);
        }
    }
}
