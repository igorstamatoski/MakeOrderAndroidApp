package com.example.makeorderandroidapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.R;

public class FavouritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtItemName;
    public ImageView imageView, fav_image;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;


    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FavouritesViewHolder(@NonNull View itemView) {
        super(itemView);

        txtItemName = (TextView) itemView.findViewById(R.id.item_name);
        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        fav_image = (ImageView) itemView.findViewById(R.id.fav);

        view_background = (RelativeLayout) itemView.findViewById(R.id.view_background);
        view_foreground = (LinearLayout) itemView.findViewById(R.id.view_foreground);

        itemView.setOnClickListener(this);
    }

    @Override

    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(), false);

    }
}
