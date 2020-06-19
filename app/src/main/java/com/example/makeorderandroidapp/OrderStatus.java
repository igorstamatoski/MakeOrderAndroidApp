package com.example.makeorderandroidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Model.Request;
import com.example.makeorderandroidapp.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        if(phone == null)
        {
            phone = Common.currentUser.getPhone();
        }

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone")
                        .equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, final int i) {

                    orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                    orderViewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                    orderViewHolder.txtOrderAddress.setText(request.getAddress());
                    orderViewHolder.txtOrderPhone.setText(request.getPhone());
                    orderViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(adapter.getItem(i).getStatus().equals("0"))
                            {
                                deleteOrder(adapter.getRef(i).getKey());
                            } else
                            {
                                Toast.makeText(OrderStatus.this, "You cannot delete this Order!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            }

            private void deleteOrder(final String key) {
                requests.child(key)
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(OrderStatus.this, new StringBuilder("Order ")
                                .append(key)
                                .append(" has been deleted!").toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
