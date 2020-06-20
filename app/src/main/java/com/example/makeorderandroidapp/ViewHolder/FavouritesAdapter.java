package com.example.makeorderandroidapp.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Model.Favourites;
import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.ItemDetails;
import com.example.makeorderandroidapp.ItemsList;
import com.example.makeorderandroidapp.Model.Order;
import com.example.makeorderandroidapp.Model.ShopItem;
import com.example.makeorderandroidapp.R;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesViewHolder> {

    private Context context;
    private List<Favourites> favouritesList;

    public FavouritesAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favourites_item, parent, false);

        return new FavouritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder itemsViewHolder, int position) {

        itemsViewHolder.txtItemName.setText(favouritesList.get(position).getItemName());

        Glide.with(context)
                .load(favouritesList.get(position).getItemImage())
                .centerCrop()
                .into(itemsViewHolder.imageView);


        final Favourites local = favouritesList.get(position);
        itemsViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                //Start new Activity
                Intent itemDetail = new Intent(context, ItemDetails.class);
                itemDetail.putExtra("ItemId",favouritesList.get(position).getItemId());
                context.startActivity(itemDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public void removeItem(int position)
    {
        favouritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favourites item, int position)
    {
        favouritesList.add(position, item);
        notifyItemInserted(position);
    }

    public Favourites getItem(int position)
    {
        return favouritesList.get(position);
    }
}
