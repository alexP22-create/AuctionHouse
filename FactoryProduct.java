package com.company;
//factory design pattern whose method returns an object
public class FactoryProduct {
    public Product createProduct(String data){
        String[] cells = data.split(" ");
        switch(cells[0]) {
            case "Painting":
                colors colorType;
                if(cells[6].equals("OIL"))
                    colorType = colors.OIL;
                else
                if(cells[6].equals("TEMPERA"))
                    colorType = colors.TEMPERA;
                else
                    colorType = colors.ACRILIC;
                //create the builder
                Painting.PaintingBuilder pb = new Painting.PaintingBuilder();
                //set all the builder's attributes
                pb.setId(Integer.parseInt(cells[1]))
                  .setName(cells[2])
                  .setMinPrice(Double.parseDouble(cells[3]))
                  .setYear(Integer.parseInt(cells[4]));
                pb.setPainterName(cells[5])
                  .setColorType(colorType);
                return pb.buildPainting();
            case "Furniture":
                Furniture.FurnitureBuilder fb = new Furniture.FurnitureBuilder();
                fb.setId(Integer.parseInt(cells[1]))
                        .setName(cells[2])
                        .setMinPrice(Double.parseDouble(cells[3]))
                        .setYear(Integer.parseInt(cells[4]));
                fb.setType(cells[5])
                  .setMaterial(cells[6]);
                return fb.buildFurniture();
            case "Jewel":
                Jewel.JewelBuilder jb = new Jewel.JewelBuilder();
                jb.setId(Integer.parseInt(cells[1]))
                        .setName(cells[2])
                        .setMinPrice(Double.parseDouble(cells[3]))
                        .setYear(Integer.parseInt(cells[4]));
                jb.setMaterial(cells[5])
                  .setGem(Boolean.parseBoolean(cells[6]));
                return jb.buildProduct();
            default:
                return null;
        }

    }
}
