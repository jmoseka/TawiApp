package com.mila.agrimo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChooseLocation extends AppCompatActivity {

     ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    String region, subRegion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        expandableListView =  findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        //get all keys from hashmap list detail
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


     /*   expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });*/


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                       region = expandableListTitle.get(groupPosition);
                       subRegion = expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition);
                Intent intent = new Intent(getApplicationContext(), PostAdvertActivity.class);
                intent.putExtra("region", region);
                intent.putExtra("subRegion", subRegion);
                setResult(Activity.RESULT_OK, intent);
                finish();

                return false;
            }
        });



    }



}