package com.jmc.foodics;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;



public class Order extends AppCompatActivity {

    FirebaseFirestore db;
    ImageView imageView;
    TextView name,about,Price;
    EditText EtAddress;
    String foodName, About_food,price ,url,ChooseItem,Quantity,Address;
    Button btnOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        imageView = findViewById(R.id.img);
        name = findViewById(R.id.name);
        about = findViewById(R.id.about);
        Price = findViewById(R.id.price);
        EtAddress = findViewById(R.id.add);
        btnOrder =findViewById(R.id.Order);

        foodName = getIntent().getStringExtra("textViewText");
        Address =EtAddress.getText().toString();

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("FoodDetails");

        collectionRef.whereEqualTo("foodName",foodName)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            About_food = documentSnapshot.getString("About_food");
                            price = documentSnapshot.getString("price");
                            url = documentSnapshot.getString("url");

                            name.setText(foodName);
                            about.setText(About_food);
                            Price.setText(price);
                            Glide.with(getApplicationContext()).load(url).into(imageView);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Fire storeData", "Error getting documents: " + e.getMessage());
                    }
                });

        Spinner spinnerNum = findViewById(R.id.Spinner);
        spinnerNum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               ChooseItem = parentView.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
        Spinner Item = findViewById(R.id.ItemSpinner);
        Item.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
             Quantity = parentView.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Order.this, CheckOutActivity.class);
                i.putExtra("foodName", foodName);
                i.putExtra("price", price);
                i.putExtra("Quantity", ChooseItem);
                i.putExtra("Address", Address);
                i.putExtra("Item", Quantity);
                startActivity(i);
            }
        });



    }



}



