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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.models.Tasks;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

//When working with fragments, instead of using this or referring to the context, always use getActivity() in intent
public class signupFragment extends Fragment {

    // keys for shared preferences
    public static final String UID = "UID";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";

    public static final String TAG = "TAG";
    EditText mName, mEmail, mPassword;
    Button mRegisterBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    String userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_register, container, false);
        mName = (EditText) v.findViewById(R.id.name);
        mEmail = (EditText) v.findViewById(R.id.edress);
        mPassword = (EditText) v.findViewById(R.id.pw);
        mRegisterBtn = (Button) v.findViewById(R.id.button1);
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        // check if user is already logged in
        /*if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getActivity(), Login.class));
            getActivity().finish();
        }*/

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String name = mName.getText().toString();

                if(TextUtils.isEmpty(name)){
                    mName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required");
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError("Password must be more than 6 characters");
                }

                // this method checks whether an email already exists in database
                mAuth.fetchSignInMethodsForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                // check if task.isSuccessful for this method, in this case is fetching email add from firestore
                                // if ! successful then display error msg
                                if (task.isSuccessful()) {
                                    // this compares user email address in sign up page to all email address in firebase
                                    // if match, return true, else false
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if (isNewUser)
                                        Log.e(TAG, "New user");

                                    // if not new user then, display message that user is already registered
                                    // Try-catch block required because it crashed the app if you key in a badly formatted email address
                                    // throwing task.getException() requires you to implement all 4 exceptions
                                    else {
                                        try {
                                            Toast.makeText(getActivity(), "This email address is already registered", Toast.LENGTH_SHORT).show();
                                            throw Objects.requireNonNull(task.getException());
                                        } catch (FirebaseAuthWeakPasswordException weakPassword) {
                                            Log.d(TAG, "onComplete: weak_password");
                                        } catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                            Log.d(TAG,"onComplete: email_badly_formatted");
                                        } catch (FirebaseAuthUserCollisionException existEmail) {
                                            Log.d(TAG, "onComplete: exist_email");
                                        } catch (Exception e) {
                                            Log.d(TAG, "onComplete: " + e.getMessage());
                                        }
                                    }
                                }
                                // There is a potential that .getMessage() might return null
                                // the task.getException() do not cover all scenarios when entering an invalid email address
                                // some of the formats might lead to a null object. If the getMessage() is null, it crashes the app.
                                // Use a ternary operator -?- to check if its null to handle it
                                // If not null, execute task.getException().getMessage(), else display my own message
                                else {
                                    Toast.makeText(getActivity(), (task.getException() != null ?
                                            task.getException().getMessage() + " Please enter a valid email format" : ""),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                //register user in firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "User created", Toast.LENGTH_SHORT).show();
                            userID = mAuth.getCurrentUser().getUid(); // get user ID in firebase
                            DocumentReference documentReference = mStore.collection("users").document(userID);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Name", name);
                            user.put("Email", email);
                            user.put("task list", new ArrayList());
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e);
                                }
                            });

                            // saves shared preferences in Shared_preferences_for_Jon.xml, private to editor
                            SharedPreferences preferences = getActivity().getSharedPreferences("Shared_preferences_for_Jon", Context.MODE_PRIVATE);
                            FirebaseUser getUser = mAuth.getCurrentUser();
                            if (getUser != null){
                                //String name = getUser.getDisplayName();
                                String email = getUser.getEmail();
                                String uid = getUser.getUid();

                                // put details into the editor, saved as key-value pairs
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(UID, uid);
                                editor.putString(NAME, name);
                                editor.putString(EMAIL, email);

                                // Find the folder on View > Tool Windows > Device File Explorer
                                // data > data > shared_pref > com.example.myapplication
                                // or just use the search button on the top right for device file explorer
                                editor.apply();

                                // test to see if data is saved
                                /*if (editor.commit())
                                    Toast.makeText(getActivity(), "Data is saved", Toast.LENGTH_LONG).show();*/
                            }


                            startActivity(new Intent(getActivity(), explore.class));
                        }

                    }
                });
            }

        });
        return v;
    }
}
