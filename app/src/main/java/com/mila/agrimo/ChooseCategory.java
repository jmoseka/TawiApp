package com.mila.agrimo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ChooseCategory extends AppCompatActivity {
    CardView  chooseVeg, chooseFruits, chooseCereals,
            chooseDiary,chooseEggs, chooseMeat,
    chooseFertilizer, chooseSeeds, choosePesticides;
    String s ,key;
    String foodMarketPlace = "Food Marketplace";
    String farmMarketplace = "Farm Marketplace";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        chooseVeg = findViewById(R.id.chooseVeg);
        chooseFruits = findViewById(R.id.chooseFruits);
        chooseCereals = findViewById(R.id.chooseCereals);
        chooseDiary = findViewById(R.id.chooseDairy);
        chooseEggs = findViewById(R.id.chooseEggs);
        chooseMeat = findViewById(R.id.chooseMeat);
        chooseFertilizer = findViewById(R.id.chooseFertilizer);
        chooseSeeds = findViewById(R.id.chooseSeeds);
        choosePesticides = findViewById(R.id.choosePesticides);




        chooseVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent returnIntent = new Intent();
                s = "Vegetables";

                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });

        chooseFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Fruits";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


        chooseCereals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Cereals";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        //chooseDiary,chooseEggs, chooseMeat

        chooseDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Dairy";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        chooseEggs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Eggs";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        chooseMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Meat";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", foodMarketPlace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        choosePesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Pesticide";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", farmMarketplace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        chooseFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Fertilizer";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", farmMarketplace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        chooseSeeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                s = "Seed";
                returnIntent.putExtra("cat", s );
                returnIntent.putExtra("key", farmMarketplace);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });


    }
}