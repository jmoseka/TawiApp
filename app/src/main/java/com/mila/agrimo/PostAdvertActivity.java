package com.mila.agrimo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PostAdvertActivity extends AppCompatActivity {
    private static final int PICKER_REQUEST_CODE = 66;
    private TextView uploadProduct;
    int imagesCount;
    private static final int LAUNCH_ACTIVITY_TW0 = 2;
    private static final int LAUNCH_LOCATION_ACTIVITY = 5;
    String[] pathsList;
    List<String> imagesAdvert;
    AdvertImagesAdapter adapter;
    private TextView sellProductDesc;
    String image="";
    String image1="";
    String image2="";
    String image3="";
    String category, title,description, location,price;
    private Button btnSell;
    private DatabaseReference oneRef,twoRef, threeRef,databaseReference, myRef,fourRef, myListing;
    private StorageReference reference;
    ProductModel model;
    FirebaseUser user;
    List<Uri> uriList;
    List<String> uploadLinks;
    AdvertInfoModel adList;
    TextView tvCategory,  additionalInfo, sellLocation, subRegion;
    ImageView btnSendCategoryMenu, chooseLocation;
    EditText etSellProductTitle, etLocation, etPrice;
    double double_random;
    String randomKey, key, country, signIn;
    String strDate, userLocation;
    LinearLayout linearInfo;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_advert);
        init();
        progressBar = findViewById(R.id.postSpin_kit);
        Sprite doubleBounce = new Circle();
        progressBar.setIndeterminateDrawable(doubleBounce);

        Random rand = new Random();
        double_random = rand.nextDouble();
        //generate random alphanumeric string

        String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";

        // combine all strings
        String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 10;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(alphaNumeric.length());

            // get character specified by index
            // from the string
            char randomChar = alphaNumeric.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        randomKey = sb.toString();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:aa", Locale.US);
       strDate = formatter.format(date);
        etSellProductTitle = findViewById(R.id.sellProductTitle);
        sellLocation = findViewById(R.id.sellLocation);
        etPrice = findViewById(R.id.etSellPrice);
        adList = new AdvertInfoModel();


        //userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
       getLocation();


     reference= FirebaseStorage.getInstance().getReference("Image Listings");

      model  = new ProductModel();
      uriList = new ArrayList<>();
      uploadLinks = new ArrayList<>();


      
        RecyclerView rvAdvertImage = findViewById(R.id.rvAdvertImage);
        rvAdvertImage.setLayoutManager(new GridLayoutManager(this, 3));
        rvAdvertImage.setHasFixedSize(true);
        adapter = new AdvertImagesAdapter(imagesAdvert, getApplicationContext(),imagesCount);
        rvAdvertImage.setAdapter(adapter);


        tvCategory = findViewById(R.id.advertCategory);



        chooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChooseLocation.class);
                startActivityForResult(intent, LAUNCH_LOCATION_ACTIVITY);

            }
        });

        btnSendCategoryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChooseCategory.class);

                startActivityForResult(intent, LAUNCH_ACTIVITY_TW0);
            }
        });

        uploadProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GligarPicker().requestCode(PICKER_REQUEST_CODE).withActivity(PostAdvertActivity.this).limit(4).show();
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !validateCategory() | !validateImage() |  !validateTitle() |
                        !validateDescription() | !validateLocation()
                        | !validatePrice()) { }

                else {
                    uploadToFirebase();
                }

            }
        });

    }



    public void getLocation() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserInfo").child(user.getUid());

       ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @org.jetbrains.annotations.NotNull DataSnapshot snapshot) {
                String s = "" + snapshot.child("country").getValue();
                initRef(s);
            }
            @Override
            public void onCancelled(@NonNull @org.jetbrains.annotations.NotNull DatabaseError error) {

            }
        });
    }

    private void initRef(String s) {
        model.setCountry(s);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Pending Listings");
        myRef = FirebaseDatabase.getInstance().getReference().child("User Pending Listings");
        oneRef = FirebaseDatabase.getInstance().getReference("All Listings");
        twoRef = FirebaseDatabase.getInstance().getReference("All Listings");
        threeRef = FirebaseDatabase.getInstance().getReference("All Listings");
        fourRef = FirebaseDatabase.getInstance().getReference("All Listings");
        oneRef = FirebaseDatabase.getInstance().getReference("All Listings");
        myListing = FirebaseDatabase.getInstance().getReference("My Listings");

    }

    private void uploadToFirebase() {


        for(int i = 0; i < uriList.size(); i++)
        {
            Uri individualImage = uriList.get(i);
            StorageReference imgName = reference.child("Image" + individualImage.getLastPathSegment());
            imgName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imgName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadUri = uriTask.getResult();

                            if(uriTask.isSuccessful()) {
                                //image uploaded
                                //add/update uri in user
                                if (uriList.size()==1){
                                    assert downloadUri != null;
                                    uploadLinks.add(downloadUri.toString());
                                    saveData();
                                }

                                else if(uriList.size() == 2){
                                    uploadLinks.add(downloadUri.toString());
                                    saveData2();
                                }

                                else if(uriList.size() == 3){
                                    uploadLinks.add(downloadUri.toString());
                                    saveData3();
                                }

                                else if(uriList.size() == 4){
                                    uploadLinks.add(downloadUri.toString());
                                    saveData4();
                                }

                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                    //Toast.makeText(PostAdvertActivity.this, "Uploading", Toast.LENGTH_SHORT).show();
                   progressBar.setVisibility(View.VISIBLE);
                    btnSell.setEnabled(false);

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(PostAdvertActivity.this, "Error"+e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    }

    private void saveData4() {

        if (uploadLinks.size() == 4){
           // spinKitView.setVisibility(View.VISIBLE);
            btnSell.setEnabled(false);
            // Toast.makeText(this, "TWO IMAGES"+uploadLinks.size(), Toast.LENGTH_SHORT).show();
            model.setImage(uploadLinks.get(0));
            model.setImage1(uploadLinks.get(1));
            model.setImage2(uploadLinks.get(2));
            model.setImage3(uploadLinks.get(3));


            model.setProductCategory(category);
            model.setProductTitle(title);
            model.setProductLocation(location);
            model.setProductCurrency(price);
            model.setProductDescription(description);
            model.setProductRandom(randomKey);
            model.setProductTime(strDate);
            model.setAdditionalInfo(subRegion.getText().toString());
            model.setSection(key);

            ProductModel p = new ProductModel(user.getUid(), randomKey, model.getProductCategory(), model.getImage(),
                    model.getImage1(), model.getImage2(), model.getImage3(),model.productTitle, model.productDescription,
                    model.productLocation, model.productCurrency, model.productTime,
                    model.getCountry(), model.getAdditionalInfo(), model.getSection());

            oneRef.child(model.getCountry()).child(model.getSection()).child(model.getProductRandom())
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            myListing.child(user.getUid()).child(model.getProductRandom())
                                    .setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

            /*databaseReference.child(randomKey)
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            myRef.child(user.getUid())
                                    .child(randomKey)
                                    .setValue(p)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("randomKey", model.getProductRandom());
                                            startActivity(intent);
                                        }
                                    });
                        }
                    });*/
    }
    }

    private void saveData3() {
      //  Toast.makeText(this, "THREE IMAGES"+uploadLinks.size(), Toast.LENGTH_SHORT).show();

        if (uploadLinks.size() == 3){
            model.setImage(uploadLinks.get(0));
            model.setImage1(uploadLinks.get(1));
            model.setImage2(uploadLinks.get(2));
            model.setImage3("");

            model.setProductCategory(category);
            model.setProductTitle(title);
            model.setProductLocation(location);
            model.setProductCurrency(price);
            model.setProductDescription(description);
            model.setProductRandom(randomKey);
            model.setProductTime(strDate);
            model.setAdditionalInfo(subRegion.getText().toString());
            model.setSection(key);

            ProductModel p = new ProductModel(user.getUid(), randomKey, model.getProductCategory(), model.getImage(),
                    model.getImage1(), model.getImage2(), model.getImage3(),model.productTitle, model.productDescription,
                    model.productLocation, model.productCurrency, model.productTime,
                    model.getCountry(), model.getAdditionalInfo(), model.getSection());

            oneRef.child(model.getCountry()).child(model.getSection()).child(model.getProductRandom())
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            myListing.child(user.getUid()).child(model.getProductRandom())
                                    .setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

          /*  databaseReference.child(randomKey)
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            myRef.child(user.getUid())
                                    .child(randomKey)
                                    .setValue(p)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("randomKey", model.getProductRandom());
                                            startActivity(intent);
                                        }
                                    });
                        }
                    });*/

    }

    }

    private void saveData2() {
        if (uploadLinks.size() == 2){
            model.setImage(uploadLinks.get(0));
            model.setImage1(uploadLinks.get(1));
            model.setImage2("");
            model.setImage3("");

            model.setProductCategory(category);
            model.setProductTitle(title);
            model.setProductLocation(location);
            model.setProductCurrency(price);
            model.setProductDescription(description);
            model.setProductRandom(randomKey);
            model.setProductTime(strDate);
            model.setAdditionalInfo(subRegion.getText().toString());
            model.setSection(key);

            ProductModel p = new ProductModel(user.getUid(), randomKey, model.getProductCategory(), model.getImage(),
                    model.getImage1(), model.getImage2(), model.getImage3(),model.productTitle, model.productDescription,
                    model.productLocation, model.productCurrency, model.productTime,
                    model.getCountry(), model.getAdditionalInfo(), model.getSection());

            oneRef.child(model.getCountry()).child(model.getSection()).child(model.getProductRandom())
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            myListing.child(user.getUid()).child(model.getProductRandom())
                                    .setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

           /* databaseReference.child(randomKey)
                    .setValue(p)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            myRef.child(user.getUid()).child(randomKey)
                                    .setValue(p)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressBar.setVisibility(View.GONE);
                                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                           // intent.putExtra("randomKey", model.getProductRandom());
                                            startActivity(intent);
                                        }
                                    });
                        }
                    });*/
        }

    }


    private void saveData() {
        if (uriList.size()==1) {
            //Toast.makeText(this, "ONE IMAGE" + uploadLinks.size(), Toast.LENGTH_SHORT).show();
            if (uriList.size() == 1){
                model.setImage(uploadLinks.get(0));
                model.setImage1("");
                model.setImage2("");
                model.setImage3("");

                model.setProductCategory(category);
                model.setProductTitle(title);
                model.setProductLocation(location);
                model.setProductCurrency(price);
                model.setProductDescription(description);
                model.setProductRandom(randomKey);
                model.setProductTime(strDate);
                model.setAdditionalInfo(subRegion.getText().toString());
                model.setSection(key);


         ProductModel p = new ProductModel(user.getUid(), randomKey, model.getProductCategory(), model.getImage(),
                 model.getImage1(), model.getImage2(), model.getImage3(),model.productTitle, model.productDescription,
                 model.productLocation, model.productCurrency, model.productTime,
                 model.getCountry(), model.getAdditionalInfo(), model.getSection());

         oneRef.child(model.getCountry()).child(model.getSection()).child(model.getProductRandom())
                 .setValue(p)
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {

                         myListing.child(user.getUid()).child(model.getProductRandom())
                                 .setValue(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void unused) {
                                 Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                 startActivity(intent);
                                 finish();
                             }
                         });
                     }
                 });



                  /* databaseReference.child(randomKey)
                           .setValue(p)
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void unused) {

                                   myRef.child(user.getUid())
                                           .child(randomKey)
                                           .setValue(p)
                                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void unused) {
                                                  progressBar.setVisibility(View.GONE);
                                                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                               // intent.putExtra("randomKey", model.getProductRandom());
                                                startActivity(intent);
                                                  // Toast.makeText(PostAdvertActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                               }
                                           });
                               }
                           });*/



               }
        }
    }


    private void generateSimpleList() {
        try {
            image =imagesAdvert.get(0);
           // Toast.makeText(this, "simple list"+imagesAdvert.size(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){

            image = "";
        }


        try {
            image1 = imagesAdvert.get(1);
        }
        catch (Exception e){

            image1 = "";
        }

        try {
            image2 = imagesAdvert.get(2);
        }
        catch (Exception e){
           // Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            image2 = "";
        }

        try {
            image3 = imagesAdvert.get(3);
        }
        catch (Exception e){
           // Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            image3 = "";
        }

        check();


    }

    private void check(){

        if(image1.isEmpty() && image2.isEmpty()&& image3.isEmpty()){
            Uri uri = Uri.fromFile(new File(image));
            uriList.clear();
            uriList.add(uri);

        } else if ( image2.isEmpty() && image3.isEmpty()  )
            {
                Uri uri = Uri.fromFile(new File(image));
                Uri uri1 = Uri.fromFile(new File(image1));
                uriList.clear();
                uriList.add(uri);
                uriList.add(uri1);


            }
        else if(image3.isEmpty())
        {
            Uri uri = Uri.fromFile(new File(image));
            Uri uri1 = Uri.fromFile(new File(image1));
            Uri uri2 = Uri.fromFile(new File(image2));
            uriList.clear();
            uriList.add(uri);
            uriList.add(uri1);
            uriList.add(uri2);
        }
        else {
            Uri uri = Uri.fromFile(new File(image));
            Uri uri1 = Uri.fromFile(new File(image1));
            Uri uri2 = Uri.fromFile(new File(image2));
            Uri uri3 = Uri.fromFile(new File(image3));
            uriList.clear();
            uriList.add(uri);
            uriList.add(uri1);
            uriList.add(uri2);
            uriList.add(uri3);
        }
        //Toast.makeText(this, ""+uriList.size(), Toast.LENGTH_SHORT).show();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            if (requestCode == LAUNCH_ACTIVITY_TW0){
                String s = data.getStringExtra("cat");
                key = data.getStringExtra("key");
                tvCategory.setText(s);
            }

            if (requestCode == LAUNCH_LOCATION_ACTIVITY){
                String a = data.getStringExtra("region");
                String b = data.getStringExtra("subRegion");

                String c = b + " " + additionalInfo.getText().toString();
                sellLocation.setText(a);
                linearInfo.setVisibility(View.VISIBLE);
                subRegion.setText(c);
            }


           if (requestCode == PICKER_REQUEST_CODE){
                pathsList = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.
                imagesCount = pathsList.length;

                imagesAdvert.addAll(Arrays.asList(pathsList));

                adapter.notifyDataSetChanged();
              // Toast.makeText(this, imagesAdvert.size(), Toast.LENGTH_SHORT).show();

               generateSimpleList();
            // trial();
            }

        }
    }


    private void init() {
        uploadProduct = findViewById(R.id.tvUploadProduct);
        sellProductDesc = findViewById(R.id.sellProductDesc);
        btnSell = findViewById(R.id.btnSell);
        imagesAdvert = new ArrayList<>();
        btnSendCategoryMenu = findViewById(R.id.chooseCat);
        user =FirebaseAuth.getInstance().getCurrentUser();
        chooseLocation = findViewById(R.id.chooseLocation);
        sellLocation = findViewById(R.id.sellLocation);
        additionalInfo = findViewById(R.id.additionalInfo);
        linearInfo = findViewById(R.id.linearInfo);
        subRegion = findViewById(R.id.subRegion);
    }


    private boolean validateImage() {
        if(image.isEmpty()){
            Toast.makeText(this, "Please upload an image of your item", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }


    private boolean validateCategory() {
        String cat = tvCategory.getText().toString().trim();
        if(cat.isEmpty()){
            tvCategory.setError("Please fill this field");
            return false;
        }
        else {
            tvCategory.setError(null);
            category = tvCategory.getText().toString().trim();
            return true;
        }
    }

    private boolean validateTitle() {
        String cat = etSellProductTitle.getText().toString().trim();
        if(cat.isEmpty()){
            etSellProductTitle.setError("Please fill this field");
            return false;
        }
        else {
            etSellProductTitle.setError(null);
            title = etSellProductTitle.getText().toString().trim();
            return true;
        }
    }

    private boolean validateLocation() {
        String cat = sellLocation.getText().toString().trim();
        if(cat.isEmpty()){
            sellLocation.setError("Please fill this field");
            return false;
        }
        else {
            sellLocation.setError(null);
            location = sellLocation.getText().toString();
            return true;
        }
    }

    private boolean validatePrice() {
        String cat = etPrice.getText().toString().trim();
        if(cat.isEmpty()){
            etPrice.setError("Please fill this field");
            return false;
        }
        else {
            etPrice.setError(null);
            price = "Ksh " + etPrice.getText().toString();
            return true;
        }
    }

    private boolean validateDescription() {

        String cat = sellProductDesc.getText().toString().trim();
        if(cat.isEmpty()){
            sellProductDesc.setError("Please fill this field");
            return false;
        }
        else {
            sellProductDesc.setError(null);
            description = sellProductDesc.getText().toString();
            return true;
        }
    }





}




