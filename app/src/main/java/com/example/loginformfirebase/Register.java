package com.example.loginformfirebase;

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

public class Register extends AppCompatActivity {

    static final String firebaseURL = "https://login-register-baebb-default-rtdb.firebaseio.com/";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(firebaseURL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        final EditText name = findViewById(R.id.fullName);
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final EditText cfmPassword = findViewById(R.id.confirmPass);
        final TextView loginNow = findViewById(R.id.loginNow);

        final Button registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String nameTxt = name.getText().toString();
                String phoneTxt = phone.getText().toString();
                String passwordTxt = password.getText().toString();
                String cfmPasswordTxt = cfmPassword.getText().toString();

                if ( nameTxt.isEmpty() || phoneTxt.isEmpty() ||
                        passwordTxt.isEmpty() || cfmPasswordTxt.isEmpty()){
                    Toast.makeText(Register.this, "You must fill all fields", Toast.LENGTH_SHORT).show();
                }
                // check if password is not matching with confirm password
                else if (!passwordTxt.equals(cfmPasswordTxt)) {
                    Toast.makeText(Register.this, "Password and Confirm Password are not matching", Toast.LENGTH_SHORT).show();
                } else {

                    // check phone has already in database
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(phoneTxt)){
                                Toast.makeText(Register.this, "Phone has been registered already", Toast.LENGTH_SHORT).show();
                            } else {
                                // post data to firebase
                                databaseReference.child("users").child(phoneTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(phoneTxt).child("password").setValue(passwordTxt);

                                Toast.makeText(Register.this, "Register successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}