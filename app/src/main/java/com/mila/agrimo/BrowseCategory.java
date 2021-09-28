package com.mila.agrimo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BrowseCategory extends AppCompatActivity {
    CardView cvVeg, cvFruits, cvMeat, cvCereals, cvEggs, cvDiary,
            cvSeeds, cvFertilizer, cvPesticide;
    String category;
    String foodMarketplace = "Food Marketplace";
    String farmMarketplace = "Farm Marketplace";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_category);
        cvVeg = findViewById(R.id.cvVeg);
        cvFruits = findViewById(R.id.cvFruits);
        cvCereals = findViewById(R.id.cvCereals);
        cvMeat = findViewById(R.id.cvMeat);
        cvEggs = findViewById(R.id.cvEggs);
        cvDiary = findViewById(R.id.cvDiary);
        cvSeeds = findViewById(R.id.cvSeeds);
        cvFertilizer = findViewById(R.id.cvFertilizer);
        cvPesticide = findViewById(R.id.cvPesticides);


       cvVeg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                   category = "Vegetables";
                   Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                   intent1.putExtra("category", category);
                   intent1.putExtra("Food Marketplace", foodMarketplace);
                   startActivity(intent1);
               finish();
           }
       });

        cvFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Fruits";
                Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Food Marketplace", foodMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvEggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Eggs";
                Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Food Marketplace", foodMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Dairy";
                Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Food Marketplace", foodMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Meat";
                Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Food Marketplace", foodMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvCereals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Cereals";
                Intent intent1 = new Intent(getApplicationContext(), FoodMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Food Marketplace", foodMarketplace);
                startActivity(intent1);
                finish();
            }
        });
        //cvSeeds, cvFertilizer, cvPesticide;

        cvSeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Seeds";
                Intent intent1 = new Intent(getApplicationContext(), FarmMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Farm Marketplace", farmMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Fertilizer";
                Intent intent1 = new Intent(getApplicationContext(), FarmMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Farm Marketplace", farmMarketplace);
                startActivity(intent1);
                finish();
            }
        });

        cvPesticide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category = "Pesticide";
                Intent intent1 = new Intent(getApplicationContext(), FarmMarketplace.class);
                intent1.putExtra("category", category);
                intent1.putExtra("Farm Marketplace", farmMarketplace);
                startActivity(intent1);
                finish();
            }
        });

    }
}