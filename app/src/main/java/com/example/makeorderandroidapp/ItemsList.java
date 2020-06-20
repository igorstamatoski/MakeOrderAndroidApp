package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Database.Database;
import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.example.makeorderandroidapp.ViewHolder.ItemsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.example.makeorderandroidapp.Model.Favourites;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ItemsList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;
    FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder> adapter;

    String categoryId = "";

    //Search Functionality
    FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    //FavouritesActivity
    Database localDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Shop");

        //Local DB
        localDB = new Database(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent from home

        if(getIntent() != null)
        {
            categoryId = getIntent().getStringExtra("CategoryId");
        }

        if(!categoryId.isEmpty() && categoryId != null)
        {
            if(Common.isConnectedToInternet(getBaseContext())) {
                loadItemsList(categoryId);
            } else {
                Toast.makeText(ItemsList.this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Search
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter your item");
        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for (String search: suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                    {
                        suggest.add(search);
                    }
                    materialSearchBar.setLastSuggestions(suggest);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if(!enabled) {
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
        
    }

    private void startSearch(CharSequence text) {

        searchAdapter = new FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder>(
                ShopItem.class,
                R.layout.shop_item,
                ItemsViewHolder.class,
                itemList.orderByChild("name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(ItemsViewHolder itemsViewHolder, ShopItem shopItem, int i) {
                itemsViewHolder.txtItemName.setText(shopItem.getName());
                Picasso.with(getBaseContext()).load(shopItem.getImage())
                        .into(itemsViewHolder.imageView);

                final ShopItem local = shopItem;
                itemsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent itemDetail = new Intent(ItemsList.this, ItemDetails.class);
                        itemDetail.putExtra("ItemId",searchAdapter.getRef(position).getKey());
                        startActivity(itemDetail);
                    }
                });
            }
        };

        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        itemList.orderByChild("catID").equalTo(categoryId)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren()){
                    ShopItem item = postSnapshot.getValue(ShopItem.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadItemsList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder>(ShopItem.class,
                R.layout.shop_item,
                ItemsViewHolder.class,
                itemList.orderByChild("catID").equalTo(categoryId)) {

            @Override
            protected void populateViewHolder(final ItemsViewHolder itemsViewHolder, final ShopItem shopItem, final int position) {
                itemsViewHolder.txtItemName.setText(shopItem.getName());

                Glide.with(getBaseContext())
                        .load(shopItem.getImage())
                        .centerCrop()
                        .into(itemsViewHolder.imageView);

                //Add FavouritesActivity
                if(localDB.isFavourite(adapter.getRef(position).getKey(), Common.currentUser.getPhone()))
                {
                    itemsViewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                }

                //Changing state of FavouritesActivity
                itemsViewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Favourites favourites = new Favourites();
                        favourites.setItemId(adapter.getRef(position).getKey());
                        favourites.setItemName(shopItem.getName());
                        favourites.setItemDescription(shopItem.getDescription());
                        favourites.setItemDiscount(shopItem.getDiscount());
                        favourites.setItemImage(shopItem.getImage());
                        favourites.setItemCatId(shopItem.getCatID());
                        favourites.setUserPhone(Common.currentUser.getPhone());
                        favourites.setItemPrice(shopItem.getPrice());


                        if(!localDB.isFavourite(adapter.getRef(position).getKey(), Common.currentUser.getPhone()))
                        {
                            localDB.addToFavourites(favourites);
                            itemsViewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(ItemsList.this,"" + shopItem.getName() + " was added to FavouritesActivity!", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.deleteFromFavourites(adapter.getRef(position).getKey(), Common.currentUser.getPhone());
                            itemsViewHolder.fav_image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(ItemsList.this,"" + shopItem.getName() + " was removed from FavouritesActivity!", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                final ShopItem local = shopItem;
                itemsViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new Activity
                        Intent itemDetail = new Intent(ItemsList.this, ItemDetails.class);
                        itemDetail.putExtra("ItemId",adapter.getRef(position).getKey());
                        startActivity(itemDetail);
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
    }
}
