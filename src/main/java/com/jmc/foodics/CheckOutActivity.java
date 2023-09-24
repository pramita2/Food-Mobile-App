package com.jmc.foodics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.khalti.checkout.helper.Config;
import com.khalti.checkout.helper.KhaltiCheckOut;
import com.khalti.checkout.helper.OnCheckOutListener;
import com.khalti.checkout.helper.PaymentPreference;
import com.khalti.widget.KhaltiButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckOutActivity extends AppCompatActivity {
        TextView tvName,tvPrice,tvAddress,tvQty,tvItem, tvTotal;
        String foodName,price,Quantity,Item,Address,email;
        FirebaseAuth auth;
        FirebaseUser user;
        FirebaseFirestore db;
        CollectionReference OrderDetailCollectionRef;
        KhaltiButton khaltiButton;
        Long total;
        int  P = 0;
        int Q = 0;
        int totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

            tvName= findViewById(R.id.name);
            tvPrice = findViewById(R.id.price);
            tvQty = findViewById(R.id.qty);
            tvItem = findViewById(R.id.Item);
            tvTotal = findViewById(R.id.total);
            tvAddress = findViewById(R.id.mAddress);


            Intent intent = getIntent();
            foodName = intent.getStringExtra("foodName");
            price = getIntent().getStringExtra("price");
            Item =  getIntent().getStringExtra("Item");
            Quantity = getIntent().getStringExtra("Quantity");
            Address = getIntent().getStringExtra("Address");

           try {
               P = Integer.parseInt(price);
               Q = Integer.parseInt(Quantity);
           }catch (NumberFormatException e){
               Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
           }


            totalAmount = P*Q;
            total = P*100L*Q;

            tvName.setText(foodName);
            tvPrice.setText(price);
            tvQty.setText(Quantity);
            tvItem.setText(Item);
            tvTotal.setText(String.valueOf(totalAmount));
            tvAddress.setText(Address);


            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            OrderDetailCollectionRef = db.collection("OrderDetail");
            user = auth.getCurrentUser();
            assert user != null;
            email = user.getEmail();


            khaltiButton = findViewById(R.id.khalti_button);

            Config.Builder builder = new Config.Builder("test_public_key_2b1af6d74f9b4135adcf9c6873b59af6", foodName, "food", total, new OnCheckOutListener() {
                @Override
                public void onError(@NonNull String action, @NonNull Map<String, String> errorMap) {
                    Log.i(action, errorMap.toString());
                    Toast.makeText(CheckOutActivity.this, "Payment Cancel", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(@NonNull Map<String, Object> data) {
                    Log.i("success", data.toString());
                    orderSave();
                }
            })
                    .paymentPreferences(new ArrayList<PaymentPreference>() {{
                        add(PaymentPreference.KHALTI);
                    }});
            Config config = builder.build();
            khaltiButton.setCheckOutConfig(config);

            KhaltiCheckOut khaltiCheckOut = new KhaltiCheckOut(this, config);

            khaltiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    khaltiCheckOut.show();
                }
            });
        }

        @SuppressLint("SetTextI18n")
        private void orderSave() {
            // Create a custom dialog
            final Dialog dialog = new Dialog(CheckOutActivity.this);
            dialog.setContentView(R.layout.dialog);

            // Find views inside the dialog
            ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
            TextView dialogText = dialog.findViewById(R.id.dialog_text);
            Button dialogOKButton = dialog.findViewById(R.id.dialog_ok);

            // Set the dialog content
            dialogImage.setImageResource(R.drawable.baseline_check_circle_24); // Set the image resource
            dialogText.setText("Order Successful"); // Set the dialog text

            // Set click listener for the OK button
            dialogOKButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Dismiss the dialog
                    dialog.dismiss();

                    // Start MainActivity
                    startActivity(new Intent(CheckOutActivity.this, MainActivity.class));
                    finish();
                }
            });

            // Show the custom dialog
            dialog.show();

            // Save booking information to Firestore
            Map<String, Object> OrderDetail = new HashMap<>();
            OrderDetail.put("order_at", FieldValue.serverTimestamp());
            OrderDetail.put("userEmail", email);
            OrderDetail.put("foodName", foodName);
            OrderDetail.put("price", price);
            OrderDetail.put("Quantity",Quantity);
            OrderDetail.put("Address",Address);
            OrderDetail.put("TotalAmount", totalAmount);


            OrderDetailCollectionRef.document().set(OrderDetail)
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // An error occurred while adding user information
                            Toast.makeText(CheckOutActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }