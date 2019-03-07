package com.domain;

import java.util.Date;

public class Book {
    private String id;
    private String title;
    private Date createdAt;
    private String author;
    private Double price;
    private Double tax;
    private String taxType;
    private Double totalCost;

    public Book(){
        this.price =1000.0;
        this.author = "Jose Long";
        this.title= "Java Cloud Native";
    }

    public Book(String title, Date createdAt, String author) {
        this.title = title;
        this.createdAt = createdAt;
        this.author = author;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }


    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createdAt=" + createdAt +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", tax=" + tax +
                ", taxType='" + taxType + '\'' +
                ", totalCost=" + totalCost +
                '}';
    }
}
