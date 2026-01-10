package com.example.finalproject;

public class Komunaluri {
    int id;
    String name;
    double amount;
    boolean paid;
    String date;

    Komunaluri(int id,String name,double amount, boolean paid, String date){
        this.id=id;
        this.name=name;
        this.amount=amount;
        this.paid=paid;
        this.date=date;

    }
    int getId(){return id;}
    String getName(){return name;}
    double getAmount(){return amount;}
    boolean isPaid(){return paid; }
    String getDate(){return date;}




    void SetName(String name){
        this.name=name;
    }
    void setAmount(double amount){ this.amount=amount; }
    void isPaid(boolean paid){ this.paid=paid; }
    void setDate(String date){ this.date=date; }
    void setId(int id){ this.id=id;}



}
