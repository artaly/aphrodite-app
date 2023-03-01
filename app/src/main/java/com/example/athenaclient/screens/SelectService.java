package com.example.athenaclient.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.athenaclient.R;
import com.example.athenaclient.ServiceInterface;
import com.example.athenaclient.adapter.ServiceAdapter;
import com.example.athenaclient.helpers.ServiceSearchHelpers;
import com.example.athenaclient.model.Service;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SelectService extends AppCompatActivity implements ServiceInterface {

    RecyclerView serviceRecyclerView;
    ArrayList<Service> serviceArrayList;
    ArrayList<Service> selectedServiceArrayList;
    ServiceAdapter serviceAdapter;
    FirebaseFirestore db;
    String branch_name;
    EditText serviceSearchBar;
    Button btnProceed;

    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        branch_name =  getIntent().getStringExtra("BRANCH_NAME");
        serviceSearchBar = findViewById(R.id.serviceSearchBar);

        backButton = findViewById(R.id.backButton);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        serviceRecyclerView = findViewById(R.id.serviceRecycler);
        btnProceed=findViewById(R.id.btnProceed);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        serviceArrayList = new ArrayList<>();
        selectedServiceArrayList = new ArrayList<Service>();
        serviceAdapter = new ServiceAdapter(SelectService.this, serviceArrayList, this);

        serviceRecyclerView.setAdapter(serviceAdapter);

        EventListener();
        setSearchListener();

        btnProceed.setOnClickListener(v -> {
            if (!selectedServiceArrayList.isEmpty()) {
               // Toast.makeText(this, "Selected services are: "+selectedServices, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SelectDate.class);
                intent.putExtra("BRANCH_NAME", branch_name);
                intent.putParcelableArrayListExtra("SELECTED_SERVICES_LIST",selectedServiceArrayList);
                //intent.putExtra("SELECTED_SERVICES", serviceArrayList.get(position).getServiceName());
               // intent.putExtra("SELECTED_SERVICES_COST", serviceArrayList.get(position).getServiceCost());
                startActivity(intent);

            }else {
                Toast.makeText(this, "Please select at least one service", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(view -> {
            super.onBackPressed();
        });

    }

    private void setSearchListener() {
        serviceSearchBar.setText("");
        findViewById(R.id.serviceSearchButton).setOnClickListener(view -> {
            try {
                View active = SelectService.this.getCurrentFocus();
                if (active != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(active.getWindowToken(), 0);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        ServiceSearchHelpers.implementserviceSearch(serviceSearchBar, serviceAdapter, serviceArrayList);
    }

    private void EventListener() {
        db.collection("services").orderBy("serviceName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                serviceArrayList.add(dc.getDocument().toObject(Service.class));

                            }
                            serviceAdapter.notifyDataSetChanged();
                        }

                    }
                });
    }

    @Override
    public void onItemClick(Service service) {
        if (selectedServiceArrayList.isEmpty()){
            selectedServiceArrayList.add(service);
        }else{
            // -1 denotes that the object doesn't exist in list
            if (selectedServiceArrayList.contains(service)){
                int position=selectedServiceArrayList.indexOf(service);
                if (!selectedServiceArrayList.isEmpty()){
                    selectedServiceArrayList.remove(position);
                }
            }else{
                selectedServiceArrayList.add(service);
            }
        }
    }
}