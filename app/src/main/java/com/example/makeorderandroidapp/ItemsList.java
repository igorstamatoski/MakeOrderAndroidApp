package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.example.makeorderandroidapp.ViewHolder.ItemsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemsList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference itemList;
    FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder> adapter;

    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_list);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        itemList = database.getReference("Shop");

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
            loadItemsList(categoryId);
        }
    }

    private void loadItemsList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<ShopItem, ItemsViewHolder>(ShopItem.class,
                R.layout.shop_item,
                ItemsViewHolder.class,
                itemList.orderByChild("CatId").equalTo(categoryId)) {

            @Override
            protected void populateViewHolder(ItemsViewHolder itemsViewHolder, ShopItem shopItem, int position) {
                itemsViewHolder.txtItemName.setText(shopItem.getName());
                Picasso.with(getBaseContext()).load(shopItem.getImage())
                        .into(itemsViewHolder.imageView);

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
