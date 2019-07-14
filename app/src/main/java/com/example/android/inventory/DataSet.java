package com.example.android.inventory;

public class DataSet {
    private String productName, description;
    private float price;
    private int quantity,id;
    private byte[] thumbnail;

    public DataSet(int id,String productName, String description, float price, int quantity, byte[] thumbnail) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.thumbnail = thumbnail;
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataSet(String productName, float price, int quantity, byte[] thumbnail) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.thumbnail = thumbnail;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
