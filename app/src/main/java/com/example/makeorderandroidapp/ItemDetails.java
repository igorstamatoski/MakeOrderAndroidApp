package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Database.Database;
import com.example.makeorderandroidapp.Model.Order;
import com.example.makeorderandroidapp.Model.Rating;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemDetails extends AppCompatActivity implements RatingDialogListener {

    TextView item_name, item_price, item_description;
    ImageView item_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart, btnRating;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    String itemId="";
    ShopItem currentItem;

    FirebaseDatabase database;
    DatabaseReference items;
    DatabaseReference ratingTable;

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String comments) {
         //Uploading rating to firebase

        final Rating rating = new Rating(Common.currentUser.getPhone(), itemId, String.valueOf(i), comments);

        ratingTable.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(Common.currentUser.getPhone()).exists())
                {
                   //Remove old value
                    ratingTable.child(Common.currentUser.getPhone()).removeValue();
                   //Update new value
                    ratingTable.child(Common.currentUser.getPhone()).setValue(rating);
                    finish();
                } else {
                    //Update new value
                     ratingTable.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(ItemDetails.this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        //Init Firebase
        database = FirebaseDatabase.getInstance();
        items = database.getReference("Shop");
        ratingTable = database.getReference("Rating");

        //Init view
        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnChart);
        btnRating = (FloatingActionButton) findViewById(R.id.btnRating);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        itemId,
                        currentItem.getName(),
                        numberButton.getNumber(),
                        currentItem.getPrice(),
                        currentItem.getDiscount(),
                        currentItem.getImage(),
                        Common.currentUser.getPhone()
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
                getRatingItem(itemId);
            } else {
                Toast.makeText(ItemDetails.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getRatingItem(String itemId) {

        Query itemRating = ratingTable.orderByChild("itemId").equalTo(itemId);

        itemRating.addValueEventListener(new ValueEventListener() {
            int count=0, sum=0;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }

                 if(count != 0)
                 {
                     float average = sum/count;
                     ratingBar.setRating(average);
                 }   
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
    }

    private void showRatingDialog(){

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Quite OK", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this item")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(ItemDetails.this)
                .show();
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
