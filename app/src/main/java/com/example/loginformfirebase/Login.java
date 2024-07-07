package com.example.loginformfirebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    static final String firebaseURL = "https://login-register-baebb-default-rtdb.firebaseio.com/";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);

        final TextView registerNow = findViewById(R.id.registerNow);

        // check behavior when click button login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneTxt = phone.getText().toString();
                String passwordTxt = password.getText().toString();

                if ( phoneTxt.isEmpty() || passwordTxt.isEmpty()){
                    // toast to info message to screen
                    Toast.makeText(Login.this, "Phone or Password is empty", Toast.LENGTH_SHORT).show();
                } else {
                    // open main activity
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(phoneTxt)){
                                final String getPassword = snapshot.child(phoneTxt).child("password").getValue(String.class);

                                if (passwordTxt.equals(getPassword)){
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, " Wrong Password", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Login.this, " Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open register activity
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}