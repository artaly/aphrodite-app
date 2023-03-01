package com.example.athenaclient.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.athenaclient.Home;
import com.example.athenaclient.R;
import com.example.athenaclient.ServiceInterface;
import com.example.athenaclient.adapter.SelectedServiceAdapter;
import com.example.athenaclient.model.Service;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Invoice extends AppCompatActivity implements ServiceInterface {

    TextView lblBranch, serviceList, txtDate, txtTime, lblCost;
    BottomNavigationView bottomNavigationView;
    ArrayList<Service> selectedServiceArrayList;
    RecyclerView serviceRecyclerView;
    SelectedServiceAdapter selectedServiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        String branch_name =  getIntent().getStringExtra("BRANCH_NAME");
        selectedServiceArrayList=new ArrayList<Service>();
        if (getIntent()!=null){
            selectedServiceArrayList=getIntent().getParcelableArrayListExtra("SELECTED_SERVICES_LIST");
        }
        int selected_service_cost =  getIntent().getIntExtra("SELECTED_SERVICES_COST", 0);
        String selected_date =  getIntent().getStringExtra("SELECTED_DATE");
        String selected_time =  getIntent().getStringExtra("SELECTED_TIME");

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        serviceRecyclerView = findViewById(R.id.serviceRecycler);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(layoutManager);
        selectedServiceAdapter = new SelectedServiceAdapter(Invoice.this, selectedServiceArrayList, this);

        serviceRecyclerView.setAdapter(selectedServiceAdapter);

        lblBranch = findViewById(R.id.lblBranch);
        serviceList = findViewById(R.id.serviceList);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        lblCost = findViewById(R.id.lblCost);

        lblBranch.setText(branch_name);
        txtDate.setText(selected_date);
        lblCost.setText(String.valueOf(selected_service_cost));
        txtTime.setText(selected_time);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.nav_book:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(Service service) {

    }
}