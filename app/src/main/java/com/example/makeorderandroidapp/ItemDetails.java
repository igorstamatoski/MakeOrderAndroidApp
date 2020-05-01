package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ItemDetails extends AppCompatActivity {

    TextView item_name, item_price, item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String itemId="";

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
            getItemDetails(itemId);
        }





    }

    private void getItemDetails(String itemId) {

        items.child(itemId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShopItem item = dataSnapshot.getValue(ShopItem.class);

                Picasso.with(getBaseContext()).load(item.getImage())
                        .into(item_image);

                collapsingToolbarLayout.setTitle(item.getName());

                item_price.setText(item.getPrice());
                item_name.setText(item.getName());
                item_description.setText(item.getDescription());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
