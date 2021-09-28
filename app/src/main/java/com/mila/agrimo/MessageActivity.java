package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    RecyclerView rvMessageUser;
    List<MyMessageUser> myMessageUserList;
    MessageUserAdapter messageAdapter;
    DatabaseReference ref;
    FirebaseUser user;
    private BottomNavigationView bottomNavigationView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();

        rvMessageUser = findViewById(R.id.rvMyMessage);
        myMessageUserList = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.messageBottomNavView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessageUser.setLayoutManager(linearLayoutManager);
        rvMessageUser.setHasFixedSize(true);
        messageAdapter = new MessageUserAdapter(this, myMessageUserList);
        rvMessageUser.setAdapter(messageAdapter);
        populateList();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.nav_community) {
                    Intent intent1 = new Intent(getApplicationContext(), BrowseCategory.class);
                    startActivity(intent1);
                    finish();

                }
                if (item.getItemId() == R.id.nav_profile) {
                    Intent intent1= new Intent(MessageActivity.this, ProfileActivity.class);
                    startActivity(intent1);
                    finish();
                }
                if (item.getItemId() == R.id.nav_home) {
                    Intent intent1= new Intent(MessageActivity.this, MainActivity.class);
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

    private void init() {
        bottomNavigationView = findViewById(R.id.messageBottomNavView);
    }


    public void populateList() {

        ref.child("My Messages").child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                       myMessageUserList.clear();
                           for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                               //(String messageUser, String lastMessage, String lastTime)
                               String id = "" + dataSnapshot.child("id").getValue();
                               String lastMessage = "" + dataSnapshot.child("lastMessage").getValue();
                               String lastTime = "" + dataSnapshot.child("lastTime").getValue();
                               String messageUsername = "" + dataSnapshot.child("messageUsername").getValue();
                               String image = "" + dataSnapshot.child("image").getValue();

                               MyMessageUser messageModel = new MyMessageUser(messageUsername, lastMessage, lastTime, id, image);
                                myMessageUserList.add(messageModel);

                            }
                            messageAdapter.notifyDataSetChanged();
                        }


                    @Override
                    public void onCancelled (@NonNull @NotNull DatabaseError error){

                    }
                });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}