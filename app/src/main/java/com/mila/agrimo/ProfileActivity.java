package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    DatabaseReference databaseReference, userRef, logOff;
     List<ProductModel> productModels;

     MyListingAdapter adapter;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    TextView profileName, profileL;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextView etEditProfile;
    CircleImageView profileImg;
    RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.profileT);

        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        bottomNavigationView = findViewById(R.id.profileBottomNavView);
        TextView signout;
        //signout = findViewById(R.id.signout);
        profileName = findViewById(R.id.profileUsername);
        etEditProfile = findViewById(R.id.etEditProfile);
        profileImg = findViewById(R.id.profileImg);
        profileL = findViewById(R.id.profileLocation);
        mRecyclerView = findViewById(R.id.rvMyListing);
        productModels = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference();

        mRecyclerView.setNestedScrollingEnabled(false);
       // mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        adapter = new MyListingAdapter(getApplicationContext(), productModels);
        mRecyclerView.setAdapter(adapter);


        getCountry();
        generateList();


        etEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_messages) {
                    Intent intent1= new Intent(getApplicationContext(), MessageActivity.class);
                    startActivity(intent1);
                    finish();
                }

                if (item.getItemId() == R.id.nav_sell) {
                    Intent intent = new Intent(ProfileActivity.this, PostAdvertActivity.class);
                    startActivity(intent);
                    finish();
                }


                if (item.getItemId() == R.id.nav_community) {
                    Intent intent1 = new Intent(getApplicationContext(), BrowseCategory.class);
                    startActivity(intent1);
                    finish();
                }
                if (item.getItemId() == R.id.nav_profile) {
                   // Intent intent1= new Intent(ProfileActivity.this, ProfileActivity.class);
                    //startActivity(intent1);
                }

                if (item.getItemId() == R.id.nav_home) {
                  Intent intent1 = new Intent(ProfileActivity.this, MainActivity.class);
                  startActivity(intent1);
                  finish();
                }

                return true;
            }
        });

    }

    private void generateList() {
        userRef.child("My Listings").child(user.getUid()).addValueEventListener(new ValueEventListener() {
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

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void getCountry() {
        assert user != null;
        logOff = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
        logOff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                String name = "" + snapshot.child("name").getValue();
                String pic = "" + snapshot.child("image").getValue();
                String location = "" + snapshot.child("location").getValue();

                profileName.setText(name);

                if (location.isEmpty()){
                }
                else {
                    profileL.setText(location);
                }

                if (pic.isEmpty()){
                }
                else {
                    Glide.with(getApplicationContext()).load(pic).
                            into(profileImg);
                }


               // myCountry(s);

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating menu

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        return true;
    }
    //handle menu item clicks


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //get item id
        firebaseAuth = FirebaseAuth.getInstance();
        int id = item.getItemId();

        if(id == R.id.action_logout){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("signedIn", "false");
            logOff.updateChildren(hashMap);
            firebaseAuth.signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkUserStatus() {
        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null)
        { }
        else {



        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}