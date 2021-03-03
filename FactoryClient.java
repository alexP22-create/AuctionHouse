package com.company;
//factory design pattern whose method returns an object
public class FactoryClient {
    public Client createClient(String data){
        String[] cells = data.split(" ");
        switch(cells[0]) {
            case "Legal":
                return new LegalPerson(cells[1], Double.parseDouble(cells[2]), cells[3]);
            case "Individual":
                return new PhysicalPerson(cells[1], Double.parseDouble(cells[2]));
            default:
                return null;
        }

    }
}
