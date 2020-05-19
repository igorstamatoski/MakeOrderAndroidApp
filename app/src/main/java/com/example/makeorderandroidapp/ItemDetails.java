package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Database.Database;
import com.example.makeorderandroidapp.Model.Order;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemDetails extends AppCompatActivity {

    TextView item_name, item_price, item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String itemId="";
    ShopItem currentItem;

    FirebaseDatabase database;
    DatabaseReference items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        items = database.getReference("Shop");

        //Init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnChart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        itemId,
                        currentItem.getName(),
                        numberButton.getNumber(),
                        currentItem.getPrice(),
                        currentItem.getDiscount()
                ));

                Toast.makeText(ItemDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        item_description = (TextView) findViewById(R.id.item_description);
        item_name = (TextView) findViewById(R.id.item_name);
        item_price = (TextView) findViewById(R.id.item_price);
        item_image = (ImageView)findViewById(R.id.img_item);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        //Get Item id from Intent
        if(getIntent() != null)
        {
            itemId = getIntent().getStringExtra("ItemId");
        }

        if(!itemId.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext())) {
                getItemDetails(itemId);
            } else {
                Toast.makeText(ItemDetails.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                return;
            }
        }





    }

    private void getItemDetails(String itemId) {

        items.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentItem = dataSnapshot.getValue(ShopItem.class);

                Glide.with(getBaseContext())
                        .load(currentItem.getImage())
                        .into(item_image);

                collapsingToolbarLayout.setTitle(currentItem.getName());
                item_price.setText(currentItem.getPrice());
                item_name.setText(currentItem.getName());
                item_description.setText(currentItem.getDescription());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
