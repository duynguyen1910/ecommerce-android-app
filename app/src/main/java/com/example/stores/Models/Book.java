package com.example.stores.Models;

import java.io.Serializable;

public class Book implements Serializable {
    private int bookID;
    private String bookName;
    private String bookImageURI;
    private String desc;
    private String author;
    private int vote;
    private int quantity;
    private int inStock;
    private int bookCategoryID;
    private int totalBooksBorrowed;

    public Book(String bookName, int quantity) {
        this.bookName = bookName;
        this.quantity = quantity;
    }

    public Book(int bookID, String bookName, String bookImageURI, String desc, int vote, int quantity, int bookCategoryID) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookImageURI = bookImageURI;
        this.desc = desc;
        this.vote = vote;
        this.quantity = quantity;
        this.bookCategoryID = bookCategoryID;
    }

//"SELECT b.bookID,  b.bookImage, b.bookName, b.author, b.description,b.inStock, sum(d.quantity)\n" +

//"SELECT b.bookID,  b.bookImage, b.bookName, b.author, b.description,b.inStock, sum(d.quantity)\n" +

    public Book(int bookID, String bookImageURI, String bookName, String author, String desc, int inStock, int bookCategoryID, int totalBooksBorrowed) {
        this.bookID = bookID;
        this.bookImageURI = bookImageURI;
        this.bookName = bookName;
        this.author = author;
        this.desc = desc;
        this.inStock = inStock;
        this.bookCategoryID = bookCategoryID;
        this.totalBooksBorrowed = totalBooksBorrowed;
    }

    public Book(int bookID, String bookImageURI, String bookName, String author, int totalBooksBorrowed) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookImageURI = bookImageURI;
        this.author = author;
        this.totalBooksBorrowed = totalBooksBorrowed;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public Book(int bookID, String bookName, String bookImageURI, String desc, String author, int inStock, int bookCategoryID) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookImageURI = bookImageURI;
        this.desc = desc;
        this.author = author;
        this.inStock = inStock;
        this.bookCategoryID = bookCategoryID;
    }


    public Book(int bookID, String bookImageURI, String bookName, String author, String desc, int quantity) {
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookImageURI = bookImageURI;
        this.desc = desc;
        this.author = author;
        this.quantity = quantity;
    }

    public Book(String bookName, String bookImageURI, String desc, String author, int quantity, int bookCategoryID) {
        this.bookName = bookName;
        this.bookImageURI = bookImageURI;
        this.desc = desc;
        this.author = author;
        this.quantity = quantity;
        this.bookCategoryID = bookCategoryID;
    }

    public Book(String bookImageURI, String bookName, int bookCategoryID) {
        this.bookImageURI = bookImageURI;
        this.bookName = bookName;
        this.bookCategoryID = bookCategoryID;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImageURI() {
        return bookImageURI;
    }

    public void setBookImageURI(String bookImageURI) {
        this.bookImageURI = bookImageURI;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBookCategoryID() {
        return bookCategoryID;
    }

    public void setBookCategoryID(int bookCategoryID) {
        this.bookCategoryID = bookCategoryID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTotalBooksBorrowed() {
        return totalBooksBorrowed;
    }

    public void setTotalBooksBorrowed(int totalBooksBorrowed) {
        this.totalBooksBorrowed = totalBooksBorrowed;
    }
}
