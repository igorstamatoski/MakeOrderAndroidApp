package com.example.makeorderandroidapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.makeorderandroidapp.Model.Favourites;
import com.example.makeorderandroidapp.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME="MakeOrderDB.db";
    private static final int DB_VER=1;
    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public List<Order> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ProductName","ProductId","Quantity","Price","Discount", "Image", "UserPhone"};
        String sqlTable="OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Order(c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount")),
                        c.getString(c.getColumnIndex("Image")),
                        c.getString(c.getColumnIndex("UserPhone"))
                        ));
            } while (c.moveToNext());
        }
        return result;
    }


    public void addToCart(Order order){
       SQLiteDatabase db = getReadableDatabase();
       String query = String.format("INSERT INTO OrderDetail(ProductId, ProductName, Quantity, Price, Discount, Image, UserPhone) VALUES ('%s','%s','%s','%s','%s','%s','%s');",
               order.getProductId(),
               order.getProductName(),
               order.getQuantity(),
               order.getPrice(),
               order.getDiscount(),
               order.getImage(),
               order.getUserPhone());
        db.execSQL(query);
    }

    public void cleanCart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    //FavouritesActivity
    public void addToFavourites(Favourites item)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favourites(ItemId, ItemName, ItemPrice, ItemCatId, ItemImage, ItemDiscount, ItemDescription, UserPhone) " +
                "VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                item.getItemId(),
                item.getItemName(),
                item.getItemPrice(),
                item.getItemCatId(),
                item.getItemImage(),
                item.getItemDiscount(),
                item.getItemDescription(),
                item.getUserPhone());
        db.execSQL(query);
    }

    public void deleteFromFavourites(String itemId, String phone)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favourites WHERE ItemId='%s' and UserPhone='%s';", itemId, phone);
        db.execSQL(query);
    }

    public boolean isFavourite(String itemId, String phone)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favourites WHERE ItemId='%s' and UserPhone='%s';", itemId, phone);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void removeFromCart(String productId, String phone) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail WHERE UserPhone='%s' and ProductId='%s'", phone, productId);
        db.execSQL(query);
    }

    public List<Favourites> getFavourites(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"UserPhone","ItemId","ItemName","ItemPrice","ItemCatId","ItemImage","ItemDiscount","ItemDescription"};
        String sqlTable="Favourites";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Favourites> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Favourites(
                        c.getString(c.getColumnIndex("ItemId")),
                        c.getString(c.getColumnIndex("ItemName")),
                        c.getString(c.getColumnIndex("ItemPrice")),
                        c.getString(c.getColumnIndex("ItemCatId")),
                        c.getString(c.getColumnIndex("ItemImage")),
                        c.getString(c.getColumnIndex("ItemDiscount")),
                        c.getString(c.getColumnIndex("ItemDescription")),
                        c.getString(c.getColumnIndex("UserPhone"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }
}
