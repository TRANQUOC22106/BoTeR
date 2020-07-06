package com.example.boter.Adapter;

public class Person {
    private String fullname,email,temp,studentID,phone;

    public Person(){}
    public Person(String email, String fullname, String phone,String studentID, String temp){
        this.fullname = fullname;
        this.temp = temp;
        this.studentID = studentID;
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFullName() {
        return fullname;
    }

    public String getTemp() {
        return temp;
    }

    public String getStudentID() {
        return studentID;
    }
}
