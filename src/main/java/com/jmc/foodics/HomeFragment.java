package com.jmc.foodics;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FirebaseFirestore fs;
    RecyclerView recyclerView;
    ArrayList<FoodList> foodList;
    FoodAdapter foodAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvFood);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(requireContext(), foodList);
        recyclerView.setAdapter(foodAdapter);
        fs = FirebaseFirestore.getInstance();
        fs.collection("FoodDetails").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("fire store error", error.getMessage());
                }
                assert value != null;
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        foodList.add(dc.getDocument().toObject(FoodList.class));
                    }
                    foodAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}