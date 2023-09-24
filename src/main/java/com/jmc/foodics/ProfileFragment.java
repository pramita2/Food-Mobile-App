package com.jmc.foodics;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.common.subtyping.qual.Bottom;


public class ProfileFragment extends Fragment {

    EditText txtName,txtEmail,txtPhone;
    FirebaseFirestore fs;
    FirebaseAuth auth;
    FirebaseUser user;
    String email,name,phone;

    Button LogOut;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        LogOut = view.findViewById(R.id.logout);
        txtName.setEnabled(false);
        txtEmail.setEnabled(false);
        txtPhone.setEnabled(false);

        auth = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();
        assert user != null;
        email = user.getEmail();


        CollectionReference collectionRef = fs.collection("users");

        //for getting data from firebase using the mName
        collectionRef.whereEqualTo("userEmail", email)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Iterate through the matching documents
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            // Get the data from the document
                            name = documentSnapshot.getString("name");
                            phone = documentSnapshot.getString("mobile no");

                            //setting data view
                            txtName.setText(name);
                            txtEmail.setText(email);
                            txtPhone.setText(phone);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e("Fire storeData", "Error getting documents: " + e.getMessage());
                    }
                });
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });

    }
}