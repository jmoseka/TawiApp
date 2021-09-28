package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseUser user;
    private String signIn;
    private NestedScrollView nes;
    private ConstraintLayout constraintLayout;
    private AppBarLayout appBar;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    RecyclerView  mRecyclerView, foodRecyclerView;
    private List<ProductModel> productModels;
    private List<FoodListModel> foodModels;
    private ProductAdapter adapter;
    private FoodListAdapter foodListAdapter;
    private DatabaseReference userRef;
    private ProgressBar progressBar;
    private TextView viewFood, viewFarm;
    private LinearLayout linFood, linFarm;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);

        progressBar = findViewById(R.id.spinKit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);
        viewFarm = findViewById(R.id.viewFarm);
        viewFood = findViewById(R.id.viewFood);
        linFarm = findViewById(R.id.linFarm);
        linFood = findViewById(R.id.linFood);

        appBar = findViewById(R.id.appbar);
        nes = findViewById(R.id.nes);
        bottomNavigationView = findViewById(R.id.bottomNavView);
        constraintLayout = findViewById(R.id.constraintLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        mRecyclerView  = findViewById(R.id.mRecyclerView);

        productModels = new ArrayList<>();
        foodModels = new ArrayList<>();

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        foodRecyclerView = findViewById(R.id.mainFoodRecyclerView);
        foodRecyclerView.setLayoutManager(layoutManager);
        foodRecyclerView.setHasFixedSize(true);
        foodListAdapter = new FoodListAdapter(foodModels, getApplicationContext());
        foodRecyclerView.setAdapter(foodListAdapter);

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        adapter = new ProductAdapter(productModels, getApplicationContext());
        mRecyclerView.setAdapter(adapter);


        getCountry();
        generateFarmList();
        generateFoodList();


        viewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FoodMarketplace.class);
                startActivity(intent);
                finish();
            }
        });

        viewFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FarmMarketplace.class);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.nav_community)  {
                    Intent intent1 = new Intent(getApplicationContext(), BrowseCategory.class);
                    startActivity(intent1);
                    finish();
                }

                if (item.getItemId() == R.id.nav_profile) {
                    if (signIn.equals("false")) {
                        popLogin();
                    } else {
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                }

                if (item.getItemId() == R.id.nav_messages) {
                    if (signIn.equals("false")) {
                        popLogin();
                    } else {
                        Intent intent1 = new Intent(getApplicationContext(),MessageActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                }

                if (item.getItemId() == R.id.nav_sell) {
                    if (signIn.equals("false")) {
                        popLogin();
                    } else {
                        Intent intent1 = new Intent(getApplicationContext(), PostAdvertActivity.class);
                        startActivity(intent1);
                        finish();

                    }
                }
                return true;
            }
        });



    }

    private void generateFoodList() {
       // progressBar.setVisibility(View.VISIBLE);

        userRef = FirebaseDatabase.getInstance().getReference();
        String foodMarketPlace = "Food Marketplace";
        userRef.child("All Listings").child("Kenya").child(foodMarketPlace).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                foodModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //productModels.clear();
                    //(String image, String productTitle, String productLocation, String productCurrency, String productTme)
                    String productTitle = "" + ds.child("productTitle").getValue();
                    String productCurrency = "" + ds.child("productCurrency").getValue();
                    String image = "" + ds.child("image").getValue();
                    String productLocation = "" + ds.child("productLocation").getValue();
                    String productTime = "" + ds.child("productTime").getValue();
                    String id = "" + ds.child("userId").getValue();
                    String productRandom = "" + ds.child("productRandom").getValue();
                    String section = "" + ds.child("section").getValue();


                    FoodListModel myListingModel   = new FoodListModel(image, productTitle , productLocation, productCurrency,productTime, productRandom);
                    myListingModel.setSection(section);
                    myListingModel.setUserId(id);
                    foodModels.add(myListingModel);

                    progressBar.setVisibility(View.GONE);
                    foodRecyclerView.setVisibility(View.VISIBLE);

                }
               foodListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

    }

    private void generateFarmList() {
       // progressBar.setVisibility(View.VISIBLE);

        userRef = FirebaseDatabase.getInstance().getReference();
        String farmMarketPlace = "Farm Marketplace";
        userRef.child("All Listings").child("Kenya").child(farmMarketPlace).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                productModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //productModels.clear();
                    //(String image, String productTitle, String productLocation, String productCurrency, String productTme)
                    String productTitle = "" + ds.child("productTitle").getValue();
                    String productCurrency = "" + ds.child("productCurrency").getValue();
                    String image = "" + ds.child("image").getValue();
                    String productLocation = "" + ds.child("productLocation").getValue();
                    String productTime = "" + ds.child("productTime").getValue();
                    String id = "" + ds.child("userId").getValue();
                    String productRandom = "" + ds.child("productRandom").getValue();
                    String section = "" + ds.child("section").getValue();


                    ProductModel myListingModel   = new ProductModel(image, productTitle , productLocation, productCurrency,productTime, productRandom);
                    myListingModel.setSection(section);
                    myListingModel.setUserId(id);
                    productModels.add(myListingModel);

                    progressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);


                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void getCountry() {
    }

    private void popLogin(){
        appBar.setVisibility(View.GONE);
        nes.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);


        constraintLayout.setVisibility(View.VISIBLE);
        viewPager.setAdapter(new AuthAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }


    private void checkUserStatus() {
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            //country = "Kenya";
            signIn = "false";
            bottomNavigationView.setVisibility(View.VISIBLE);
            linFood.setVisibility(View.VISIBLE);
            linFarm.setVisibility(View.VISIBLE);

        }
        else {
            DatabaseReference logOff = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());
            logOff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    signIn = "" + snapshot.child("signedIn").getValue();
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    linFood.setVisibility(View.VISIBLE);
                    linFarm.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
        }
    }


    public final boolean isInternetOn() {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            showDialog();
            return false;
        }
        return false;
    }

    public  void showDialog(){
        dialog.setContentView(R.layout.nointernet_layout);

        TextView retry = dialog.findViewById(R.id.try_again);
       // LinearLayout noInternet = dialog.findViewById(R.id.no_internet_layout);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                //dialog.dismiss();
            }
        });

        dialog.show();



    }



    @Override
    protected void onStart() {
        super.onStart();
        if(!isInternetOn()){

        }
        else {
            checkUserStatus();
        }

    }
}