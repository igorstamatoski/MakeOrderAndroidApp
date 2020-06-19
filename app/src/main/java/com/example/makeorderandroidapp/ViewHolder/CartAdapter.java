package com.example.makeorderandroidapp.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.Model.Order;
import com.example.makeorderandroidapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;




public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> orders = new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> oreders, Context context) {
        this.orders = oreders;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Glide.with(context).load(orders.get(position).getImage())
          .centerCrop()
                .into(holder.cart_image);

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+orders.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en","DE");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(orders.get(position).getPrice())) * (Integer.parseInt(orders.get(position).getQuantity()));

        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(orders.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public Order getItem(int position)
    {
        return orders.get(position);
    }

    public void removeItem(int position)
    {
        orders.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Order item, int position)
    {
        orders.add(position, item);
        notifyItemInserted(position);
    }
}
