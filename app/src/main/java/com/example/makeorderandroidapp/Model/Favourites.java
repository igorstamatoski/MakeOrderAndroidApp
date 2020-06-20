package com.example.makeorderandroidapp.Model;

public class Favourites {
    private String ItemId, ItemName, ItemPrice, ItemCatId, ItemImage, ItemDiscount, ItemDescription, UserPhone;

    public Favourites() {
    }

    public Favourites(String itemId, String itemName, String itemPrice, String itemCatId, String itemImage, String itemDiscount, String itemDescription, String userPhone) {
        ItemId = itemId;
        ItemName = itemName;
        ItemPrice = itemPrice;
        ItemCatId = itemCatId;
        ItemImage = itemImage;
        ItemDiscount = itemDiscount;
        ItemDescription = itemDescription;
        UserPhone = userPhone;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemPrice() {
        return ItemPrice;
    }

    public void setItemPrice(String itemPrice) {
        ItemPrice = itemPrice;
    }

    public String getItemCatId() {
        return ItemCatId;
    }

    public void setItemCatId(String itemCatId) {
        ItemCatId = itemCatId;
    }

    public String getItemImage() {
        return ItemImage;
    }

    public void setItemImage(String itemImage) {
        ItemImage = itemImage;
    }

    public String getItemDiscount() {
        return ItemDiscount;
    }

    public void setItemDiscount(String itemDiscount) {
        ItemDiscount = itemDiscount;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
