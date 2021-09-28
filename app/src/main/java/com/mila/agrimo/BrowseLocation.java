package com.mila.agrimo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class BrowseLocation extends AppCompatActivity {
    String  location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_location);

        ListView listView;
        ArrayList<String> list;
        ArrayAdapter<String> adapter;



        listView =  findViewById(R.id.lv1);

        //init array list
        list = new ArrayList<>();

        list.add("COUNTRYWIDE");
        list.add("NAIROBI");
        list.add("MOMBASA");
        list.add("KISUMU");
        list.add("NAKURU");


        //init adapter and put the list of fruits in it
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

     listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             location = parent.getItemAtPosition(position).toString();
             Intent intent = new Intent(getApplicationContext(), PostAdvertActivity.class);
             intent.putExtra("location", location);
             setResult(Activity.RESULT_OK, intent);
             finish();
         }
     });


    }


}