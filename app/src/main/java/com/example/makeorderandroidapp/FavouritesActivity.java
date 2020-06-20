package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Database.Database;
import com.example.makeorderandroidapp.Helper.RecycleItemTouchHelper;
import com.example.makeorderandroidapp.Interface.RecyclerItemTouchHelperListener;
import com.example.makeorderandroidapp.Model.Favourites;
import com.example.makeorderandroidapp.Model.Order;
import com.example.makeorderandroidapp.ViewHolder.FavouritesAdapter;
import com.example.makeorderandroidapp.ViewHolder.FavouritesViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class FavouritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FavouritesAdapter adapter;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_favourites);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Swipe to delete
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecycleItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        loadFavourites();
    }

    private void loadFavourites() {
        adapter = new FavouritesAdapter(this, new Database(this).getFavourites());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof FavouritesViewHolder)
        {
            String name = ((FavouritesAdapter)recyclerView.getAdapter()).getItem(position).getItemName();

            final Favourites deleteItem = ((FavouritesAdapter)recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).deleteFromFavourites(deleteItem.getItemId(), Common.currentUser.getPhone());

            //Make Snackbar
            Snackbar snackbar = Snackbar.make(rootLayout,name + " removed from favourites!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToFavourites(deleteItem);
                }
            });

            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
