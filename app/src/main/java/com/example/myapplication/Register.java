package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mName, mEmail, mPassword;
    Button mRegisterBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.name);
        mEmail = findViewById(R.id.edress);
        mPassword = findViewById(R.id.pw);
        mRegisterBtn = findViewById(R.id.button1);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();


        // check if user is already logged in
        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        // setOnClickListener takes in a Class view with OnClickListener static method
        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mName.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6){
                    mPassword.setError("Password must be more than 6 characters");
                }

                //register user in firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> { // anonymous class can be replaced by lambda
                    if  (task.isSuccessful()){
                        Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                        userID = mAuth.getCurrentUser().getUid(); // get user ID in firebase
                        DocumentReference documentReference = mStore.collection("users").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("Name", name);
                        user.put("Email", email);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                    else {
                        Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });


    }
}
