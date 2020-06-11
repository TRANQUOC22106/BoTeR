package com.example.boter.Adapter;

import java.util.Date;

public class Person {
    private String name;
    private int number;
    private String nhietDo;

    public Person(String name,String nhietDo, int number){
        this.name = name;
        this.number = number;
        this.nhietDo = nhietDo;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public int getNumber(){
        return number;
    }
    public void setNumber(int Number){
        this.number = number;
    }
    public String getNhietDo(){
        return nhietDo;
    }
    public void setNhietDo(String nhietDo){
        this.nhietDo = nhietDo;
    }
}
