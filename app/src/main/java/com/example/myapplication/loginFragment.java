package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

// Create a Fragment class that returns the layout that you just created in the onCreateView() method.
// You can then create instances of this fragment in the parent activity whenever you need a new page to display to the user:
// This class is to create pages for the views, not fragment

public class loginFragment extends Fragment{

    EditText mEmail, mPassword;
    Button mLoginBtn;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.activity_login, container, false);
        mLoginBtn = (Button) v.findViewById(R.id.button3);
        mEmail = (EditText) v.findViewById(R.id.edress);
        mPassword = (EditText) v.findViewById(R.id.pw);
        mAuth = FirebaseAuth.getInstance();
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

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

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                        else
                            Toast.makeText(getActivity(), "Invalid Credentials! "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
        return v;
    }

}
