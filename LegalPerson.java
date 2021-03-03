package com.company;

public class LegalPerson extends Client{
    private Company company;
    private double socialCapital;
    public LegalPerson(String name, double buget, String company){
        super(name, buget);
        if(company.equals("SRL"))
            this.company = Company.SRL;
        else
            this.company = Company.SA;
    }
}
