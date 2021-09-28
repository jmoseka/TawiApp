package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import java.util.List;

public class OtherProfile extends AppCompatActivity {

    DatabaseReference databaseReference, userRef, ref;
    List<ProductModel> models;
    RecyclerView rvListings;
    OtherListingAdapter adapter;
    FirebaseUser user;
    ImageView profileImage;
    String otherId, country;
    TextView profileName, profilePhone, profileEmail, profileLocation, otherChat;
    BottomNavigationView bottomNavigationView;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);

        Intent intent = getIntent();
        otherId = intent.getStringExtra("userId");
        bottomNavigationView = findViewById(R.id.otherProfileBottomNavView);
        TextView signout;
       // signout = findViewById(R.id.signout);
        profileImage = findViewById(R.id.otherProfileImage);
        profileName = findViewById(R.id.otherProfileUsername);
        profileLocation = findViewById(R.id.otherProfileLocation);
        profilePhone = findViewById(R.id.otherPhoneNumber);
        rvListings = findViewById(R.id.rvOtherListing);
        otherChat = findViewById(R.id.otherChat);
        models = new ArrayList<>();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseDatabase firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        userRef = firebaseDatabase1.getReference("My Listings");

        rvListings.setNestedScrollingEnabled(false);
        // rvListings.setLayoutManager(new GridLayoutManager(this, 2));
        rvListings.setLayoutManager(new LinearLayoutManager(this));
                rvListings.setHasFixedSize(true);
        adapter = new OtherListingAdapter(getApplicationContext(), models);
        rvListings.setAdapter(adapter);



        getCountry();
        generateOtherSimpleList();
        //generateOtherProfileDetail();


        assert user != null;

        otherChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
                intent1.putExtra("id", otherId);
                startActivity(intent1);
            }
        });

        profilePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phone)));
                startActivity(intent1);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_community) {
                    Intent intent1= new Intent(OtherProfile.this, BrowseCategory.class);
                    startActivity(intent1);
                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent intent1= new Intent(OtherProfile.this, ProfileActivity.class);
                    startActivity(intent1);
                }
                if (item.getItemId() == R.id.nav_messages) {
                    Intent intent1 = new Intent(OtherProfile.this, MessageActivity.class);
                    startActivity(intent1);
                }

                if (item.getItemId() == R.id.nav_home) {
                    Intent intent1 = new Intent(OtherProfile.this, MainActivity.class);
                    startActivity(intent1);
                }

                if (item.getItemId() == R.id.nav_sell) {
                    Intent intent = new Intent(getApplicationContext(), PostAdvertActivity.class);
                    startActivity(intent);
                }

                return true;
            }
        });

    }

    private void getCountry() {

        ref = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(otherId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                String s = "" + snapshot.child("country").getValue();
                myCountry(s);
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void myCountry(String c) {
        country =c;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("UserInfo").child(otherId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot ds) {
                String name = "" + ds.child("name").getValue();
                String email = "" + ds.child("email").getValue();
                String phon = "" + ds.child("phoneNo").getValue();
                String image = "" + ds.child("image").getValue();
                String location = "" + ds.child("location").getValue();


                profileName.setText(name);
                profilePhone.setText(phon);
                //profileEmail.setText(email);
                profileLocation.setText(location);

                Glide.with(getApplicationContext()).load(image).
                        into(profileImage);
                phone = phon;


            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });


    }

    private void generateOtherSimpleList() {
        userRef.child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {

                models.clear();
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
                    models.add(myListingModel);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }



}