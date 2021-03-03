package com.company;

public class Painting extends Product{
    private String painterName;
    private colors colorType;
    public Painting(PaintingBuilder pb){
        super(pb);
        this.painterName = pb.painterName;
        this.colorType = pb.colorType;
    }
    public static class PaintingBuilder extends Product.ProductBuilder{
        private String painterName;
        colors colorType;

        public PaintingBuilder setPainterName(String painterName) {
            this.painterName = painterName;
            return this;
        }

        public PaintingBuilder setColorType(colors colorType) {
            this.colorType = colorType;
            return this;
        }

        //we will create a paintingBuilder with all the necessary attributes
        public PaintingBuilder(){

        }

        public Painting buildPainting(){
            return new Painting(this);
        }
    }
}
