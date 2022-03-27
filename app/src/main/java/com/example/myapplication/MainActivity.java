package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;


public class MainActivity extends AppCompatActivity {

    TextView textViewMsg;

    final String node = "current_msg";
    // reference that points to a subtree, root in database, dictionary
    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewMsg = findViewById(R.id.button2);
        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRef = mRootDatabaseRef.child(node);

        // get message whenever there is database change in the node (realtime update)
        mNodeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // cast snapshot back to string
                String change = snapshot.getValue(String.class);
                // update textview whenever database is changed
                textViewMsg.setText(change);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        mNodeRef.setValue(timestamp.toString());
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut(); //logout
        startActivity(new Intent(getApplicationContext(), Login.class)); //redirect to Login.class
        finish();
    }
}