package com.example.makeorderandroidapp.Model;

import androidx.annotation.NonNull;

public class ShopItem {

    private String Name;
    private String Image;
    private String Description;
    private String Price;
    private String Discount;
    private String CatId;

    public ShopItem() {
    }

    public ShopItem(String name, String image, String description, String price, String discount, String catID) {
        Name = name;
        Image = image;
        Description = description;
        Price = price;
        Discount = discount;
        CatId = catID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCatID() {
        return CatId;
    }

    public void setCatID(String catID) {
        CatId = catID;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: "+this.Name+"\nCategory: "+this.CatId;
    }
}
