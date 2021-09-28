package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

public class ChatActivity extends AppCompatActivity {
    ImageView btnSend, profileChat;
    EditText etSendMessage;
    TextView chatUserStatus, nameChat;
    FirebaseUser user;
    String receiverUid;
    FirebaseDatabase database, database1;
    String senderRoom, receiverRoom, country, onlinestatus, receiverName;
    RecyclerView messageRv;
    DatabaseReference chatRef, ref, receiverRef, userDbRef, countryRef;
    ArrayList<MessageModel> messageModelList;
    MessageAdapter messageAdapter;
    ValueEventListener seenListener;
    AppBarLayout userBar;
    private String stringMessage;
    private byte encryptionKey[] = {9, 115, 51, 86, 105, 4, -31, -23, -68, 88, 17, 20, 3, -105, 119, -53};
    private Cipher cipher, decipher;
    private SecretKeySpec secretKeySpec;
    String userId, myUsername, receiverUsername, myPic, receiverPic, receiverCountry;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        receiverUid = intent.getStringExtra("id");

        nameChat = findViewById(R.id.nameChat);
        btnSend = findViewById(R.id.btnSend);
        etSendMessage = findViewById(R.id.etSendMessage);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        database1 = FirebaseDatabase.getInstance();
        messageRv = findViewById(R.id.messageAdapter);
        messageModelList = new ArrayList<>();
        chatUserStatus = findViewById(R.id.chatUserStatus);
        userBar = findViewById(R.id.userChatBar);
        profileChat = findViewById(R.id.profileChat);

        userId = receiverUid;
        //getUserInfo();

        senderRoom = user.getUid() + receiverUid;
        receiverRoom = receiverUid + user.getUid();
        ref = database1.getReference().child("My Messages").child(receiverUid);
        receiverRef = database.getReference().child("Receiver ID").child(user.getUid());
        getUserInfo();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        messageRv.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(this, messageModelList);
        messageRv.setAdapter(messageAdapter);
        populateList();
       // Toast.makeText(this, country, Toast.LENGTH_SHORT).show();




        profileChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), OtherProfile.class);
                intent1.putExtra("userId", receiverUid);
                startActivity(intent1);
                finish();
            }
        });




        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = etSendMessage.getText().toString();

                if (m.isEmpty()){

                }
                else {
                    sendMessage(m);
                    etSendMessage.setText("");

                }

            }
        });


    }

    private void sendMessage(String m) {

        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveDate = currentDate.format(date.getTime());


        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currentTime  = new SimpleDateFormat("hh:mm a");
        final String saveTime = currentTime.format(ctime.getTime());

        MessageModel message = new MessageModel(m,user.getUid(), receiverUid, saveTime, saveDate);
        database.getReference().child("Chats")
                .child(senderRoom)
                .child("Messages")
                .push()
                .setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {

                database.getReference().child("Chats")
                        .child(receiverRoom)
                        .child("Messages")
                        .push()
                        .setValue(message)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                String b = message.getDate() + " " + message.getTime();

                                MyMessageUser my = new MyMessageUser(nameChat.getText().toString(),message.getMessage(),
                                       b, user.getUid(), receiverPic);
                                //messageUername,lastMessage, lastTime;

                                database1.getReference().child("My Messages")
                                        .child(receiverUid) //choco
                                        .child(user.getUid())
                                        .setValue(my)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                MyMessageUser ms = new MyMessageUser(receiverName,message.getMessage(),
                                                        b, receiverUid, receiverPic);

                                                database1.getReference().child("My Messages")
                                                        .child(user.getUid()) //wanja
                                                        .child(receiverUid)
                                                        .setValue(ms).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {


                                                    }
                                                });


                                            }
                                        });
                            }
                        });
            }
        });
    }


    private void populateList() {


        chatRef = FirebaseDatabase.getInstance().getReference().child("Chats").child(senderRoom).child("Messages");
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                messageModelList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    //(String message, String senderId, String receiverId, String time, String date)
                    String message = "" + ds.child("message").getValue();
                 String senderId = "" + ds.child("senderId").getValue();
                    String receiverId = "" + ds.child("receiverId").getValue();
                    String time = "" + ds.child("time").getValue();
                    String date = "" + ds.child("date").getValue();

                    MessageModel messageModel = new MessageModel(message, senderId, receiverId, time, date);
                    messageModelList.add(messageModel);
                }

                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }




    private void seenMessage() {
        seenListener = chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    MessageModel m = ds.getValue(MessageModel.class);
                    if (m.getSenderId().equals(user.getUid()) && m.getReceiverId().equals(receiverUid)){
                        HashMap<String, Object>hasSeen = new HashMap<>();
                        hasSeen.put("seen", true);
                        ds.getRef().updateChildren(hasSeen);
                    }


                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    private void getCountryName() {
        countryRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());

        countryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                country = "" + snapshot.child("country").getValue();
               onlinestatus = "" + snapshot.child("online").getValue();

               getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
            }
        });
    }



    private void getUserInfo() {
        userDbRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(receiverUid);


        userDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot ds) {

                    String name = ""+ds.child("name").getValue();
                    String image = ""+ds.child("image").getValue();
                    String onlineStatus = ""+ds.child("lastSeen").getValue();

                    receiverName = name;
                    nameChat.setText(name);
                    chatUserStatus.setText(onlineStatus);

                    Glide.with(getApplicationContext()).load(image).
                            into(profileChat);
                    receiverPic = image;


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }



    private void checkOnlineStatus(String status) {

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastSeen", status);

        //update value of online status current user
        dbRef.updateChildren(hashMap);
       // chatUserStatus.setText(status);

    }



    @Override
    protected void onStart() {
        //checkUserStatus();
        checkOnlineStatus("online");

        super.onStart();
        //set online

    }

    @Override
    protected void onPause() {
        super.onPause();

        Calendar date = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        final String saveDate = currentDate.format(date.getTime());


        Calendar ctime = Calendar.getInstance();
        SimpleDateFormat currentTime  = new SimpleDateFormat("hh:mm a");
        final String saveTime = currentTime.format(ctime.getTime());
        //get time
        String time = saveTime + " "+ saveDate;

        checkOnlineStatus(time);
       // chatRef.removeEventListener(seenListener);

    }

    @Override
    protected void onResume() {

        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


}

