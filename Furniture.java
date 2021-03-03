package com.company;

public class Furniture extends Product{
    private String type;
    private String material;
    //the object is build by using a builder
    public Furniture(FurnitureBuilder fb){
        super(fb);
        this.type = fb.type;
        this.material = fb.material;
    }
    public static class FurnitureBuilder extends Product.ProductBuilder{
        private String material;
        private String type;

        public FurnitureBuilder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public FurnitureBuilder setType(String type) {
            this.type = type;
            return this;
        }

        //we will create a productFurniture with all the necessary attributes
        public FurnitureBuilder(){

        }

        public Furniture buildFurniture(){
            return new Furniture(this);
        }
    }
}
