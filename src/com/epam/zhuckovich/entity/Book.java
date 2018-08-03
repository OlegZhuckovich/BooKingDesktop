package com.epam.zhuckovich.entity;

import java.io.Serializable;
import java.util.Date;

public class Book implements Serializable{

    public Book(String title, String author, Genre genre, String publishingHouse, int year, int pages, int quantity){
        this.title=title;
        this.author=author;
        this.genre=genre;
        this.publishingHouse=publishingHouse;
        this.year=year;
        this.pages=pages;
        this.quantity=quantity;
    }

    private int bookID;
    private String title;
    private String author;
    private Genre genre;
    private String publishingHouse;
    private int year;
    private int pages;
    private int quantity;

    private Date orderDate;
    private Date returnDate;

    public void setBookID(int bookID){
        this.bookID=bookID;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public void setPublishingHouse(String publishingHouse){
        this.publishingHouse=publishingHouse;
    }

    public void setYear(int year){
        this.year=year;
    }

    public void setPages(int pages){
        this.pages=pages;
    }

    public void setQuantity(int quantity){
        this.quantity=quantity;
    }

    public void setOrderDate(Date orderDate){
        this.orderDate=orderDate;
    }

    public void setReturnDate(Date returnDate){
        this.returnDate=returnDate;
    }

    public int getBookID(){
        return bookID;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public Genre getGenre(){
        return genre;
    }

    public String getPublishingHouse(){
        return publishingHouse;
    }

    public int getYear(){
        return year;
    }

    public int getPages(){
        return pages;
    }

    public int getQuantity(){
        return quantity;
    }

    public Date getOrderDate(){
        return orderDate;
    }

    public Date getReturnDate(){
        return returnDate;
    }

    @Override
    public String toString(){
        return "Название: " + this.title + "\nАвтор: " + this.author + "\nЖанр: " + this.genre.toString()
                + "\nИздательство: " + this.publishingHouse + "\nГод издания: " + this.year + "\nКоличество страниц: "
                + this.pages + "\nКоличество экземпляров: " + this.quantity + "\n";
    }

}