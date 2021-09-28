package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductInfoActivity extends AppCompatActivity {

    SliderAdapter sliderAdapter;
    DatabaseReference ref, userRef, countryRef, logOff;
    FirebaseUser user;
    ArrayList<SliderData> sliderDataArrayList;
    String randomKey, id;
    SliderData s;
    List<ProductInfoModel> productInfo;
    TextView productInfoName, productInfoPrice, productInfoDesc, productInfoLocation, sellerName, sellerDate, infoCancelBtn;
    CardView cvSellerInfo;
    String country, section, userId, phoneNo, signIn;
    Button btnChat, btnCall;
    CircleImageView sellerImg;
    LinearLayout linBtn;
    ConstraintLayout constraintInfo;
    ScrollView scrollInfo;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        init();
        linBtn = findViewById(R.id.Linbtn);
        constraintInfo = findViewById(R.id.constraintInfo);
        scrollInfo = findViewById(R.id.scrollInfo);
        viewPager = findViewById(R.id.viewPagerInfo);
        tabLayout = findViewById(R.id.tabLayoutInfo);
        infoCancelBtn = findViewById(R.id.infoCancelBtn);

        Intent intent = getIntent();
        randomKey = intent.getStringExtra("randomKey");
        section = intent.getStringExtra("section");
        id = intent.getStringExtra("userId");

     sliderDataArrayList = new ArrayList<>();
     productInfo = new ArrayList<>();

        SliderView sliderView = findViewById(R.id.slider);
        sliderAdapter = new SliderAdapter(this, sliderDataArrayList);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.startAutoCycle();
        getCountryName();
       // populateList();
       // populateSellerDetail(userId);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNo)));
                startActivity(intent1);
            }
        });

        infoCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollInfo.setVisibility(View.VISIBLE);
                linBtn.setVisibility(View.VISIBLE);

                //appBar.setVisibility(View.GONE);
                constraintInfo.setVisibility(View.GONE);

                viewPager.setAdapter(new AuthAdapter(getSupportFragmentManager()));
                tabLayout.setupWithViewPager(viewPager);

            }
        });


        cvSellerInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signIn.equals("false")) {
                    popLogin();
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), OtherProfile.class);
                    intent1.putExtra("userId", userId);
                    startActivity(intent1);
                    finish();
                }


            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (signIn.equals("false")) {
                    popLogin();
                } else {
                    Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
                    intent1.putExtra("id", userId);
                    startActivity(intent1);
                    finish();
                }









            }
        });
    }


    private void popLogin(){

        scrollInfo.setVisibility(View.GONE);
        linBtn.setVisibility(View.GONE);

        //appBar.setVisibility(View.GONE);
        constraintInfo.setVisibility(View.VISIBLE);

        viewPager.setAdapter(new AuthAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

    }


    private void init() {
        productInfoName = findViewById(R.id.productTitle);
        productInfoDesc = findViewById(R.id.productInfoDesc);
        productInfoLocation = findViewById(R.id.productInfoLocation);
        productInfoPrice = findViewById(R.id.productInfoPrice);
        cvSellerInfo = findViewById(R.id.cvSellerInfo);
        sellerName = findViewById(R.id.sellerName);
        btnChat = findViewById(R.id.btnChat);
        sellerDate = findViewById(R.id.sellerRegistered);
        btnCall = findViewById(R.id.btnCall);
        sellerImg = findViewById(R.id.sellerProfile);
    }

    private void getCountryName() {
        countryRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(id);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            country = "Kenya";
            signIn = "false";
            populateList(country);

        } else {
            countryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    country = "" + snapshot.child("country").getValue();
                    signIn = "" + snapshot.child("signedIn").getValue();


                    populateList(country);
                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
                }
            });
        }
    }


    private void populateList(String s) {
        ref = FirebaseDatabase.getInstance().getReference().child("All Listings").child(s).child(section);
        //Toast.makeText(getApplicationContext(), country, Toast.LENGTH_SHORT).show();
       Query query = ref.orderByChild("productRandom").equalTo(randomKey);
      query.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                      // productModels.clear();
                       for (DataSnapshot ds : snapshot.getChildren()) {
                           //productModels.clear();
                           //(String image, String productTitle, String productLocation, String productCurrency, String productTme)
                           String id = "" + ds.child("userId").getValue();
                           String productTitle = "" + ds.child("productTitle").getValue();
                           String productCurrency = "" + ds.child("productCurrency").getValue();
                           String image = "" + ds.child("image").getValue();
                           String image1 = "" + ds.child("image1").getValue();
                           String image2 = "" + ds.child("image2").getValue();
                           String image3 = "" + ds.child("image3").getValue();
                           String productLocation = "" + ds.child("productLocation").getValue();
                           String productTime = "" + ds.child("productTime").getValue();
                           String productRandom = "" + ds.child("productRandom").getValue();
                           String productDescription = "" + ds.child("productDescription").getValue();

                           userId = id;

                           if (!image.isEmpty() && image1.isEmpty() && image2.isEmpty() && image3.isEmpty()){
                               SliderData s = new SliderData(image);
                               sliderDataArrayList.add(s);

                               populateSellerDetail(userId);
                           }

                           else if (!image.isEmpty() && !image1.isEmpty() && image2.isEmpty() && image3.isEmpty()){
                               SliderData s = new SliderData(image);
                               SliderData s1 = new SliderData(image1);
                               sliderDataArrayList.add(s);
                               sliderDataArrayList.add(s1);

                               populateSellerDetail(userId);

                           }

                           else if (!image.isEmpty() && !image1.isEmpty() && !image2.isEmpty() && image3.isEmpty()){
                               SliderData s = new SliderData(image);
                               SliderData s1 = new SliderData(image1);
                               SliderData s3 = new SliderData(image2);
                               sliderDataArrayList.add(s);
                               sliderDataArrayList.add(s1);
                               sliderDataArrayList.add(s3);

                               populateSellerDetail(userId);
                           }

                           else {
                               SliderData s = new SliderData(image);
                               SliderData s1 = new SliderData(image1);
                               SliderData s3 = new SliderData(image2);
                               SliderData s4 = new SliderData(image3);
                               sliderDataArrayList.add(s);
                               sliderDataArrayList.add(s1);
                               sliderDataArrayList.add(s3);
                               sliderDataArrayList.add(s4);

                        
                               populateSellerDetail(userId);
                           }

                           productInfoName.setText(productTitle);
                           productInfoDesc.setText(productDescription);
                           productInfoPrice.setText(productCurrency);
                           productInfoLocation.setText(productLocation);
                           productInfoName.setText(productTitle);



                       }


                    sliderAdapter.notifyDataSetChanged();
                       //populateSellerDetail();
                   }
                   @Override
                   public void onCancelled (@NonNull @NotNull DatabaseError error){

                   }
               });


       //
    }

    private void populateSellerDetail(String s) {
        userRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(s);
       // Query query = userRef.orderByChild("id").equalTo(id);

               userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                String name = "" + snapshot.child("name").getValue();
                String image = "" + snapshot.child("image").getValue();
                String date = "" + snapshot.child("dateOfReg").getValue();
                String phone = "" + snapshot.child("phoneNo").getValue();

                sellerName.setText(name);
                sellerDate.setText(date);

                if (image.isEmpty()){

                }else {
                    Glide.with(getApplicationContext()).load(image).
                            into(sellerImg);
                }
                phoneNo = phone;


            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }



    private void checkUserStatus() {
        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null){
            //country = "Kenya";
            signIn = "false";
            linBtn.setVisibility(View.VISIBLE);

        }
        else {
            logOff = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());
            logOff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                    signIn = "" + snapshot.child("signedIn").getValue();
                    String s = "" + snapshot.child("country").getValue();
                    linBtn.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }
}