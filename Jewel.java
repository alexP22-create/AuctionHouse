package com.company;

public class Jewel extends Product{
    private String material;
    private boolean gem;
    public Jewel(){
    }
    public Jewel(JewelBuilder jb){
        super(jb);
        this.material = jb.material;
        this.gem = jb.gem;
    }
    public static class JewelBuilder extends Product.ProductBuilder {
        //optional attributes
        private String material;
        private boolean gem;

        public JewelBuilder setMaterial(String material) {
            this.material = material;
            return this;
        }

        public JewelBuilder setGem(boolean gem) {
            this.gem = gem;
            return this;
        }

        public JewelBuilder(){
        }
        public Jewel buildJewel(){
            return new Jewel(this);
        }
    }
}
