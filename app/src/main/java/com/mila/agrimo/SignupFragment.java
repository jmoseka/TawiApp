package com.mila.agrimo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class SignupFragment extends Fragment {
    Intent intent;
    String phone, password, email,name, code, country;
    TextInputEditText etPhoneSignUp;
    TextInputEditText etEmailSignUp;
    TextInputEditText etPasswordSignUp;
    TextInputEditText etUsernameSignUp;
    CountryCodePicker codePicker;
    DatabaseReference ref, dbRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_signup, container, false);

        codePicker = view.findViewById(R.id.codePicker);
        Button signUp = view.findViewById(R.id.signUpBtn);
        etPhoneSignUp = view.findViewById(R.id.etPhoneSignup);
        etEmailSignUp = view.findViewById(R.id.etEmailSignup);
        etPasswordSignUp = view.findViewById(R.id.etPasswordSignup);
        etUsernameSignUp = view.findViewById(R.id.etNameSignup);
        code = codePicker.getSelectedCountryCodeWithPlus();



     signUp.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {

                if (!validateUsername() | !validateEmail() | !validatePassword() | !validatePhone()) {

             } else {

                 FirebaseAuth mAuth = FirebaseAuth.getInstance();
                 mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                                if (task.isSuccessful()){
                                    Date date = new Date();
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy", Locale.US);
                                    String strDate = formatter.format(date);

                                    Calendar ctime = Calendar.getInstance();
                                    SimpleDateFormat currentTime  = new SimpleDateFormat("hh:mm a");
                                    final String saveTime = currentTime.format(ctime.getTime());


                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                                    assert user != null;
                                    MyUsers myUsers = new MyUsers(user.getUid(),name, email, phone, password,
                                            "",country, "", strDate, saveTime);

                                    dbRef = database.getReference().child("Users").child(myUsers.getCountry());
                                    dbRef.child(user.getUid()).
                                            setValue(myUsers);

                                    ref = database.getReference().child("UserInfo");

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("name", name);
                                    hashMap.put("password", password);
                                    hashMap.put("id", user.getUid());
                                    hashMap.put("image", "");
                                    hashMap.put("country", country);
                                    hashMap.put("signedIn", "true");
                                    hashMap.put("lastSeen", strDate + saveTime);
                                    hashMap.put("location", "");
                                    hashMap.put("verified", "no");
                                    hashMap.put("phoneNo", myUsers.getPhoneNo());
                                    hashMap.put("email", myUsers.getEmail());
                                    hashMap.put("dateOfReg", myUsers.getDateOfReg());


                                    ref.child(user.getUid()).
                                            setValue(hashMap);

                                    Intent intent = new Intent(getActivity(), SuccessActivity.class);
                                    startActivity(intent);
                                    Objects.requireNonNull(getActivity()).finish();

                                }

                                else {
                                    Toast.makeText(getActivity(), "Problem signing in", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                         .addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull @NotNull Exception e) {
                                 Toast.makeText(getActivity(), "ERROR: " + e, Toast.LENGTH_SHORT).show();

                             }
                         });
             }

         }
     });




        return view;
    }



    private boolean validatePhone() {
        phone = etPhoneSignUp.getText().toString().trim();
        if(phone.isEmpty()){
            etPhoneSignUp.setError("Please fill this field");
            return false;
        }
        else {
            etPhoneSignUp.setError(null);

            phone = Integer.valueOf(phone).toString();
            phone = code+phone;
          // country = codePicker.getSelectedCountryName();
            country = "Kenya";



            return true;
        }
    }

    private boolean validatePassword() {
        password = etPasswordSignUp.getText().toString().trim();
        if(password.isEmpty()){
            etPasswordSignUp.setError("Please fill this field");
            return false;
        }
        else {
            etPasswordSignUp.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        email = etEmailSignUp.getText().toString().trim();
        if(email.isEmpty()){
            etEmailSignUp.setError("Please fill this field");
            return false;
        }
        else {
            etEmailSignUp.setError(null);


            return true;
        }
    }

    private boolean validateUsername() {
        name = etUsernameSignUp.getText().toString().trim();
        if(name.isEmpty()){
            etUsernameSignUp.setError("Please fill this field");
            return false;
        }
        else {
            etUsernameSignUp.setError(null);


            return true;
        }
    }


}