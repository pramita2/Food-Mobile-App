package com.jmc.foodics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, new HomeFragment()).commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.btnNav);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle item clicks here
                int itemId = item.getItemId();
                if (itemId == R.id.Home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame,new HomeFragment()).commit();
                }else if (itemId == R.id.Detail) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame,new DetailFragment()).commit();
                }else if (itemId == R.id.Profile) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame,new ProfileFragment()).commit();
                }
                        return true;
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}