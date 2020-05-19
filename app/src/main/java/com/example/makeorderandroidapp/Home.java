package com.example.makeorderandroidapp;

import android.content.Intent;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.example.makeorderandroidapp.Common.Common;
import com.example.makeorderandroidapp.Interface.ItemClickListener;
import com.example.makeorderandroidapp.Model.Category;
import com.example.makeorderandroidapp.Service.ListenOrder;
import com.example.makeorderandroidapp.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseDatabase database;
    DatabaseReference category;

    TextView txtFullName;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    NavigationView navigationView;


    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        




        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
       NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
       NavigationUI.setupWithNavController(navigationView, navController);

      //Set Name for user
        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

       //Load menu

        recycler_menu = (RecyclerView)findViewById(R.id.recyler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        //Set Navigation Listener
        setNavigationViewListener();

        if(Common.isConnectedToInternet(this)) {
            loadMenu();
        } else{
            Toast.makeText(this,"Please check your internet connection!",Toast.LENGTH_SHORT).show();
            return;
        }

        //Register Service
        Intent service = new Intent(Home.this, ListenOrder.class);
        startService(service);
    }

    private void setNavigationViewListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenu() {

            adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder menuViewHolder, Category category, int i) {

                menuViewHolder.txtMenuName.setText(category.getName());

                Glide.with(getBaseContext())
                        .load(category.getImage())
                        .centerCrop()
                        .into(menuViewHolder.imageView);

                final Category clickItem = category;
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get category Id and send to ItemsList Activity
                        Intent  itemList = new Intent(Home.this, ItemsList.class);
                        itemList.putExtra("CategoryId", adapter.getRef(position).getKey());

                        startActivity(itemList);

                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.refresh: {
                //do something
                loadMenu();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch(menuItem.getItemId()){

            case R.id.nav_shop: {
                //do somthing
                break;
            }
            case R.id.nav_cart: {
                //do somthing
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
                break;
            }
            case R.id.nav_orders: {
                //do somthing
                Intent orderIntent = new Intent(Home.this, OrderStatus.class);
                startActivity(orderIntent);
                break;
            }
            case R.id.nav_log_out: {
                //do somthing
                Intent signIn = new Intent(Home.this, SignIn.class);
                signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signIn);
                break;
            }
        }

        return true;
    }
}
