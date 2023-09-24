package com.jmc.foodics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText Fullname,Email,Phoneno,Password,ConfPassword,Address;

    String userName,email,pass,cpass,number,address;
    String userId;
    FirebaseAuth Auth;
    FirebaseFirestore DB;
    CollectionReference UserCollRef;
    Button btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Auth = FirebaseAuth.getInstance();
        DB = FirebaseFirestore.getInstance();
        UserCollRef = DB.collection("users");

        btn = findViewById(R.id.regist);
        Fullname = findViewById(R.id.person);
        Email = findViewById(R.id.Email);
        Phoneno = findViewById(R.id.phone);
        Password = findViewById(R.id.Pwd);
        ConfPassword = findViewById(R.id.Cpass);
        Address = findViewById(R.id.add);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userName = Fullname.getText().toString();
                email = Email.getText().toString();
                pass = Password.getText().toString();
                cpass = ConfPassword.getText().toString();
                address = Address.getText().toString();
                number = Phoneno.getText().toString();

                // Regex pattern for phone and password
                String phoneRegex = "^98\\d{8}$";
                String passwordRegex = "^(?=.[0-9])(?=.[a-z])(?=.*[A-Z]).{8,}$";
                String emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                Pattern phonePattern = Pattern.compile(phoneRegex);
                Matcher phoneMatcher = phonePattern.matcher(number);

                Pattern passwordPattern = Pattern.compile(passwordRegex);
                Matcher passwordMatcher = passwordPattern.matcher(pass);

                Pattern emailPattern = Pattern.compile(emailRegex);
                Matcher emailMatcher = emailPattern.matcher(email);

                if(userName.isEmpty()){
                    Fullname.setError("Please Enter Your Name");
                    Fullname.requestFocus();
                    return;
                }
                if(address.isEmpty()) {
                    Address.setError("Please Enter Your Current Address");
                    Address.requestFocus();
                    return;
                }
                if(email.isEmpty()){
                    Email.setError("Please Enter Your Email");
                    Email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Invalid email address");
                    Email.requestFocus();
                    return;
                }
                if (pass.isEmpty()) {
                    Password.setError("Enter the password");
                    Password.requestFocus();
                    return;
                }
                if (pass.length() < 8) {
                    Password.setError("Password must be 8 Character");
                    Password.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(cpass)){
                    ConfPassword.setError("Enter Confirm Password");
                    ConfPassword.requestFocus();
                    return;

                }
                if(!pass.equals(cpass)){
                    ConfPassword.setError("Password Dosen't Match");
                    ConfPassword.requestFocus();
                    return;

                }
                if (!phoneMatcher.matches()) {
                    Phoneno.setError("Invalid phone number");
                    Phoneno.requestFocus();
                    return;
                }
                Auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser user = Auth.getCurrentUser();
                            assert user != null;
                            userId = user.getUid();

                            addNewUser();
                            Toast.makeText(RegisterActivity.this,"Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this,"Registration Failed! Try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void addNewUser(){
        Map<String, Object> user = new HashMap<>();
        user.put("Uid", userId);
        user.put("name", userName);
        user.put("userEmail", email);
        user.put("created_at", FieldValue.serverTimestamp());
        user.put("password", pass);
        user.put("mobile no",number);
        user.put("address",address);

        UserCollRef.document().set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User information added successfully
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while adding user information
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}