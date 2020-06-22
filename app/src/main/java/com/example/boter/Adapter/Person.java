package com.example.boter.Adapter;

public class Person {
    private String name;
    private int temp;
    private int userId;

    public Person(){}
    public Person(String name, int temp, int userId){
        this.name = name;
        this.temp = temp;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public int getTemp() {
        return temp;
    }

    public int getUserId() {
        return userId;
    }
}
