package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.opensooq.supernova.gligar.GligarPicker;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private static final int PICKER_REQUEST_CODE = 10;
    DatabaseReference countryRef, databaseReference, ref;
    FirebaseUser user;
    String country;
    TextView email, phone;
    EditText location;
    CircleImageView image;
    Button btnUpdate, btnUpdateImage, btnCancel;
    Uri myUri;
    String myUrl, mImage, mLocation;
    StorageReference reference;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();

        countryRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
        btnCancel = findViewById(R.id.btnCancelEdit);
        email = findViewById(R.id.editProfileEmail);
        phone = findViewById(R.id.editProfileNumber);
        location = findViewById(R.id.editProfileLocation);
        image = findViewById(R.id.editProfileImage);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdateImage = findViewById(R.id.btnUpdateImg);
        reference= FirebaseStorage.getInstance().getReference("Profile Images");

        progressBar = findViewById(R.id.spinUpload);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        getCountryName();
        //userDetail();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myUri != null & !location.getText().toString().isEmpty()){
                   uploadImageToFirebase();
                   saveLocation();
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                   // Toast.makeText(EditProfileActivity.this, "Not empty ", Toast.LENGTH_SHORT).show();
                }
               else if (myUri == null && !location.getText().toString().isEmpty()){
                   saveLocation();
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                   // Toast.makeText(EditProfileActivity.this, "Text is not empty, image is empty", Toast.LENGTH_SHORT).show();

                }
               else if (location.getText().toString().isEmpty() && myUri!= null){
                   uploadImageToFirebase();
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();

                }
               else{
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).
                        withActivity(EditProfileActivity.this).limit(1).show();
            }
        });
    }

    private void saveLocation() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users").child(country).child(user.getUid());

        String l = location.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("location", l);

        countryRef.updateChildren(hashMap);
        ref.updateChildren(hashMap);
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }


    private void getCountryName() {
        countryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                country = "" + snapshot.child("country").getValue();
               // Toast.makeText(EditProfileActivity.this, country, Toast.LENGTH_SHORT).show();
                userDetail(country);
               // populateList();
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {
            }
        });
    }





    private void userDetail(String s) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(s).child(user.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot ds) {

                    String em = "" + ds.child("email").getValue();
                    String pho = "" + ds.child("phoneNo").getValue();
                    String img = "" + ds.child("image").getValue();
                    String place = "" + ds.child("location").getValue();

                    phone.setText(pho);
                    email.setText(em);

                    if (img.isEmpty()){
                        mImage ="true";

                    }
                    else {
                        Glide.with(getApplicationContext()).load(img).
                                into(image);
                        mImage = "false";
                    }
                    if (place.isEmpty()){
                       mLocation ="true";
                    }
                    else {
                        location.setText(place);
                        mLocation = "false";
                    }
            }

            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });

    }

    private void uploadImageToFirebase(){
            StorageReference imgName = reference.child("Image");
      //  + myUri.getLastPathSegment()

            imgName.putFile(myUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();

                            if (uriTask.isSuccessful()) {
                                //image uploaded
                                //add/update uri in user
                                myUrl = downloadUri.toString();
                                saveImage();

                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    //Toast.makeText(getApplicationContext(), "Uploading", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.VISIBLE);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error " + e, Toast.LENGTH_SHORT).show();
                        }
                    });

    }


    private void saveImage() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users").child(country).child(user.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("image", myUrl);

        countryRef.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                ref.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKER_REQUEST_CODE){
            //assert data != null;
            if (data == null){

            }
            else {
                String[] pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.

                Glide.with(getApplicationContext()).load(pathsList[0]).
                        into(image);
                myUri = Uri.fromFile(new File(pathsList[0]));
            }
        }



    }
}