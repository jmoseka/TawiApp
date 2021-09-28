package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FarmMarketplace extends AppCompatActivity {
    private static final int LAUNCH_ACTIVITY_LOCATION = 100;
    RecyclerView mRecyclerView;
    ProductAdapter adapter;
    List<ProductModel> productModels;
    List<SliderData> slideImageModel;
    DatabaseReference countryRef, foodRef, ref;
    FirebaseUser user;
    String country, signIn;
    String foodMarketplace = "Farm Marketplace";
    ProductModel model;
    TextView foodBrowseLocation;
    BannerAdapter sliderAdapter;
    SliderView sliderView;
    SearchView searchView;
    BottomNavigationView bottomNavigationView;
    AppBarLayout appBar;
    NestedScrollView nes;
    ViewPager viewPager;
    TabLayout tabLayout;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_marketplace);

        init();
        searchView = findViewById(R.id.searchView);

        slideImageModel = new ArrayList<>();
        sliderView = findViewById(R.id.food_advert_slider);
        sliderAdapter = new BannerAdapter(getApplicationContext(), slideImageModel);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.startAutoCycle();
        populateFoodList();

        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        getCountryName();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //call when user press search button from keyboard
                //if search query is not empty then search
                if(!TextUtils.isEmpty(query.trim())){
                    //search text contains text, search it
                    searchUsers(query);                }
                else{
                    getCountryName();
                    //getAllUser();
                    //search text empty, get all users
                }

                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText.trim())){
                    searchUsers(newText);                }
                else{
                    getCountryName();
                }
                return false;
            }
        });

        foodBrowseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), BrowseLocation.class);
                startActivityForResult(intent1, LAUNCH_ACTIVITY_LOCATION);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_messages) {
                    Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
                    startActivity(intent);
                    finish();
                }

                if (item.getItemId() == R.id.nav_community) {
                    Intent intent = new Intent(getApplicationContext(), BrowseCategory.class);
                    startActivity(intent);
                    finish();

                }
                if (item.getItemId() == R.id.nav_profile) {

                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

                if (item.getItemId() == R.id.nav_home) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    finish();
                }

                if (item.getItemId() == R.id.nav_sell) {
                    Intent intent = new Intent(getApplicationContext(), PostAdvertActivity.class);
                    startActivity(intent);
                    finish();

                }



                return true;
            }
        });

    }

    private void searchUsers(String query) {
        foodRef = FirebaseDatabase.getInstance().getReference()
                .child("All Listings").child(country).child(foodMarketplace);

        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                productModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String productTitle = "" + ds.child("productTitle").getValue();
                    String productCurrency = "" + ds.child("productCurrency").getValue();
                    String image = "" + ds.child("image").getValue();
                    String productLocation = "" + ds.child("productLocation").getValue();
                    String productTime = "" + ds.child("productTime").getValue();
                    String productRandom = "" + ds.child("productRandom").getValue();
                    String section = "" + ds.child("section").getValue();
                    String id = "" + ds.child("userId").getValue();

                    // SpecificItemModel specificItemModel = new SpecificItemModel();

                    if(productTitle.toLowerCase().contains(query.toLowerCase()))
                    {
                        model = new ProductModel(image, productTitle , productLocation,
                                productCurrency,productTime, productRandom);
                        model.setSection(section);
                        model.setUserId(id);
                        productModels.add(model);
                    }
                }

                adapter = new ProductAdapter(productModels, getApplicationContext());
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


    private void populateFoodList() {
        SliderData s = new SliderData(R.drawable.banana_img);
        SliderData s1 = new SliderData(R.drawable.apples);
        SliderData s3 = new SliderData(R.drawable.asian_img);

        slideImageModel.add(s);
        slideImageModel.add(s1);
        slideImageModel.add(s3);
        //slideImageModel.add(s2);

        sliderAdapter.notifyDataSetChanged();
    }

    private void getCountryName() {

        if (user == null) {
            signIn = "false";
            country = "Kenya";
            generateFoodList();
        }

        else
        {
            bottomNavigationView.setVisibility(View.VISIBLE);
        countryRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());
        countryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                country = "" + snapshot.child("country").getValue();
                signIn = "" + snapshot.child("signedIn").getValue();
                generateFoodList();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
            }
        });
    }
    }




    private void generateFoodList() {
        foodRef = FirebaseDatabase.getInstance().getReference()
                .child("All Listings").child(country).child(foodMarketplace);


        foodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                productModels.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String productTitle = "" + ds.child("productTitle").getValue();
                    String productCurrency = "" + ds.child("productCurrency").getValue();
                    String image = "" + ds.child("image").getValue();
                    String productLocation = "" + ds.child("productLocation").getValue();
                    String productTime = "" + ds.child("productTime").getValue();
                    String productRandom = "" + ds.child("productRandom").getValue();
                    String section = "" + ds.child("section").getValue();
                    String id = "" + ds.child("userId").getValue();

                    model = new ProductModel(image, productTitle , productLocation,
                            productCurrency,productTime, productRandom);
                    model.setSection(section);
                    model.setUserId(id);
                    productModels.add(model);

                }
                adapter = new ProductAdapter(productModels, getApplicationContext());
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void getLocation() {

        if (foodBrowseLocation.getText().toString().equals("COUNTRYWIDE")) {
            getCountryName();

        } else {

            ref = FirebaseDatabase.getInstance().getReference().child("All Listings").child(country).child(foodMarketplace);

            Query query = ref.orderByChild("productLocation")
                    .equalTo(foodBrowseLocation.getText().toString());

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    productModels.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String productTitle = "" + ds.child("productTitle").getValue();
                        String productCurrency = "" + ds.child("productCurrency").getValue();
                        String image = "" + ds.child("image").getValue();
                        String productLocation = "" + ds.child("productLocation").getValue();
                        String productTime = "" + ds.child("productTime").getValue();
                        String productRandom = "" + ds.child("productRandom").getValue();
                        String section = "" + ds.child("section").getValue();
                        String id = "" + ds.child("userId").getValue();

                        model = new ProductModel(image, productTitle, productLocation, productCurrency, productTime, productRandom);
                        model.setSection(section);
                        model.setUserId(id);
                        productModels.add(model);

                    }
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == LAUNCH_ACTIVITY_LOCATION) {
                assert data != null;
                String s = data.getStringExtra("location");
                foodBrowseLocation.setText(s);
                getLocation();
            }
        }
    }



    private void init() {
        mRecyclerView = findViewById(R.id.farmRecyclerView);
        productModels = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        foodBrowseLocation = findViewById(R.id.farmBrowseLocation);
        slideImageModel = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.farmBottomNavView);
        nes = findViewById(R.id.farmNes);
        appBar = findViewById(R.id.farmAppbar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        constraintLayout = findViewById(R.id.farmConstraintLayout);
    }
}