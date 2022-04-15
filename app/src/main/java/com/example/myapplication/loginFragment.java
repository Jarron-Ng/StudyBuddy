package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

// Create a Fragment class that returns the layout that you just created in the onCreateView() method.
// You can then create instances of this fragment in the parent activity whenever you need a new page to display to the user:
// This class is to create pages for the views, not fragment

public class loginFragment extends Fragment{

    // variables for shared preferences
    public static final String UID = "UID";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";

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

                // get user data from firebase and store into shared preferences
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            // saves shared preferences in Shared_preferences_for_Jon.xml, private to editor
                            SharedPreferences preferences = getActivity().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
                            FirebaseUser getUser = mAuth.getCurrentUser();
                            if (getUser != null){
                                String email = getUser.getEmail();
                                String uid = getUser.getUid();
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(UID, uid);
                                editor.putString(EMAIL, email);

                            }
                        }
                        else
                            Toast.makeText(getActivity(), "Invalid Credentials! "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //query firestore for username
                FirebaseUser getUser = mAuth.getCurrentUser();
                String uid = getUser.getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            String name = (String) document.getData().get("Name");

                            // put details into the editor, saved as key-value pairs
                            SharedPreferences preferences = getActivity().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString(UID, uid);
                            editor.putString(NAME, name);
                            editor.putString(EMAIL, email);
                            editor.putString("Tasks", "");

                            // Find the folder on View > Tool Windows > Device File Explorer
                            // data > data > shared_pref > com.example.myapplication
                            // or just use the search button on the top right for device file explorer
                            editor.commit();
                            Log.i("firebase", "successful save user details to sharedpref");
                            startActivity(new Intent(getActivity(), explore.class)); // NOTE: big J i changed this frm main activity to explore which is my landing page class
                        }
                        else {
                            Log.i("firebase", "query firestore for uid failed to complete");
                        }
                    }
                });

                // test to see if data is saved
                                /*if (editor.commit())
                                    Toast.makeText(getActivity(), "Data is saved", Toast.LENGTH_LONG).show();*/
            }
        });
        return v;
    }

}