package com.epam.zhuckovich.entity;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable{

    public Person(String name, String surname, String email, String password, Date registrationDate, Gender gender, boolean isLibrarian){
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.password=password;
        this.registrationDate=registrationDate;
        this.gender=gender;
        this.isLibrarian=isLibrarian;

        this.oldEmail=email;
        this.oldPassword=password;
    }

    public Person(int personID, String name, String surname, String email,  Date registrationDate, Gender gender, int bookOrdered){
        this.personID=personID;
        this.name=name;
        this.surname=surname;
        this.email=email;
        this.registrationDate=registrationDate;
        this.gender=gender;
        this.bookOrdered=bookOrdered;

        this.oldEmail=email;
        this.oldPassword=password;
    }

    private int personID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Date registrationDate;
    private Gender gender;
    private boolean isLibrarian;
    private int bookOrdered;

    private String oldEmail;
    private String oldPassword;

    public void setID(int id){
        this.personID=id;
    }

    public void setBookOrdered(int bookOrdered){
        this.bookOrdered=bookOrdered;
    }

    public void setName(String name){ this.name=name;}

    public void setSurname(String surname){ this.surname=surname;}

    public void setEmail(String email){
        oldEmail=this.email;
        this.email=email;
    }

    public void setPassword(String password){
        oldPassword=this.password;
        this.password=password;
    }

    public int getPersonID(){
        return personID;
    }

    public int getBookOrdered(){return bookOrdered;}

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isLibrarian() {
        return isLibrarian;
    }

    public String getOldEmail(){
        return oldEmail;
    }

    public String getOldPassword(){
        return oldPassword;
    }

    @Override
    public String toString(){
        return "Person:\nИмя: " + name + "\nФамилия: "+ surname + "\nEmail: " + email + "\nПароль: " + password + "\nДата регистрации: " + registrationDate + "\nПол: " + gender + "\nБиблиотекарь: " + isLibrarian + "\n";
    }
}
