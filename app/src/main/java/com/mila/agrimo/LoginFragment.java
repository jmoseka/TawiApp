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

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;


public class LoginFragment extends Fragment {

    TextInputEditText etEmail, password;
    Button btnLogin;
    DatabaseReference signIn;
    FirebaseUser user;
    String pass, email, id;
    FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_login, container, false);

        etEmail = view.findViewById(R.id.loginEtEmail);
        password = view.findViewById(R.id.loginEtPassword);
        btnLogin = view.findViewById(R.id.btn_login);

        mAuth = FirebaseAuth.getInstance();



       btnLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if (!validateEmail() || !validatePassword()){}
               else {
                   mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                           if (task.isSuccessful()) {
                               user = FirebaseAuth.getInstance().getCurrentUser();
                               signIn = FirebaseDatabase.getInstance().getReference("UserInfo")
                                       .child(user.getUid());
                               HashMap<String, Object> hashMap = new HashMap<>();
                               hashMap.put("signedIn", "true");


                               signIn.updateChildren(hashMap);
                               Intent intent = new Intent(getActivity(), MainActivity.class);
                               startActivity(intent);
                               Objects.requireNonNull(getActivity()).finish();

                           }

                         else {
                               Toast.makeText(getActivity(), "Problem signing in ", Toast.LENGTH_SHORT).show();

                           }


                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull @NotNull Exception e) {
                           Toast.makeText(getActivity(), "ERROR: "+ e, Toast.LENGTH_SHORT).show();

                       }
                   });

               }


           }
       });
















        return view;
    }

    private boolean validateEmail() {
        email= etEmail.getText().toString().trim();
        if(email.isEmpty()){
            etEmail.setError("Please fill this field");
            return false;
        }
        else {
            etEmail.setError(null);

            return true;
        }
    }

    private boolean validatePassword() {
        pass=password.getText().toString().trim();
        if(pass.isEmpty()){
            password.setError("Please fill this field");
            return false;
        }
        else {
            password.setError(null);

            return true;
        }
    }





    @Override
    public void onStart() {
        super.onStart();

    }


}