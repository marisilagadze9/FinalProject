package com.example.finalproject;

public class Komunaluri {
    String name;
    String icon;
    double current;

    Komunaluri(String name,String icon,double current){
        this.name=name;
        this.icon=icon;
        this.current=current;

    }
    String getName(){return name;}

    String GetIcon(){return icon;}

    double GetCurrent(){return current;}

    void SetName(String name){
        this.name=name;
    }

    void SetIcon(String icon){
        this.icon=icon;
    }

    void Setcurrent(double current){
        this.current=current;
    }

}
