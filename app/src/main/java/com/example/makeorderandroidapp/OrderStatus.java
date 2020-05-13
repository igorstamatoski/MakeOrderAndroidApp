package com.example.makeorderandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Model.Request;
import com.example.makeorderandroidapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.sql.CommonDataSource;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase Init
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if(getIntent() == null)
        {
            loadOrders(Common.currentUser.getPhone());
        } else
        {
            loadOrders(getIntent().getStringExtra("userPhone"));
        }


    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone")
                .equalTo(phone)

        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {

                    orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                    orderViewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                    orderViewHolder.txtOrderAddress.setText(request.getAddress());
                    orderViewHolder.txtOrderPhone.setText(request.getPhone());
            }
        };



        recyclerView.setAdapter(adapter);
    }
}
