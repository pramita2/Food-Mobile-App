package com.jmc.foodics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    EditText email ;
    Button forget;
    FirebaseAuth auth;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        email = findViewById(R.id.Email);
        forget = findViewById(R.id.forgetp);

        auth = FirebaseAuth.getInstance();

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validData();
            }
        });

    }
    private void validData() {
        userEmail = email.getText().toString();
        if(userEmail.isEmpty()){
            email.setError("enter your email");
        }else {
            forgetPassword();
        }
    }

    private void forgetPassword() {
        auth.sendPasswordResetEmail(userEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPassword.this, "Check Your Email", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPassword.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ForgetPassword.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}