package com.example.athenaclient.screens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athenaclient.R;
import com.example.athenaclient.ServiceInterface;
import com.example.athenaclient.adapter.SelectedServiceAdapter;
import com.example.athenaclient.adapter.ServiceAdapter;
import com.example.athenaclient.model.Service;

import java.util.ArrayList;

import com.example.athenaclient.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity implements ServiceInterface {

    TextView txtBranch, txtDate, txtTime, txtTotal;
    Button btnSubmit;
    ImageView backButton;
    FirebaseFirestore db;
    ArrayList<Service> selectedServiceArrayList;
    String branch_name="";
    String selected_date="";
    String selected_time="";
    RecyclerView serviceRecyclerView;
    SelectedServiceAdapter selectedServiceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        selectedServiceArrayList=new ArrayList<Service>();
        if (getIntent()!=null){
            branch_name =  getIntent().getStringExtra("BRANCH_NAME");
            selectedServiceArrayList=getIntent().getParcelableArrayListExtra("SELECTED_SERVICES_LIST");
            selected_date =  getIntent().getStringExtra("SELECTED_DATE");
            selected_time =  getIntent().getStringExtra("SELECTED_TIME");
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        serviceRecyclerView = findViewById(R.id.serviceRecycler);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setLayoutManager(layoutManager);
        selectedServiceAdapter = new SelectedServiceAdapter(Checkout.this, selectedServiceArrayList, this);

        serviceRecyclerView.setAdapter(selectedServiceAdapter);
        db = FirebaseFirestore.getInstance();
        backButton = findViewById(R.id.backButton);

        txtBranch = findViewById(R.id.txtBranch);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtTotal = findViewById(R.id.txtTotal);

        txtBranch.setText(branch_name);
        txtDate.setText(selected_date);
        txtTime.setText(selected_time);
        txtTotal.setText(calculateTotal() + " PHP");

        btnSubmit = findViewById(R.id.btnConfirm);

        txtTime.setText(selected_time);


        backButton.setOnClickListener(view -> {
            super.onBackPressed();
        });


        btnSubmit.setOnClickListener(view -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                String email = user.getEmail();
                String uid = user.getUid();

                Map<String, Object> booking = new HashMap<>();
                booking.put("branch", branch_name);
                booking.put("date", selected_date);
                booking.put("services", selectedServiceArrayList);
                booking.put("total", calculateTotal());
                booking.put("status", "New");
                booking.put("time", selected_time);
                booking.put("client", email);

                String id = db.collection("bookings").document().getId();


                db.collection("bookings").document(id)
                        .set(booking)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                updateUserTimeSlots();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });



            }


        });


    }

    boolean isUserTimeSlotUpdated = false;
    boolean isBranchTimeSlotUpdated =false;
    private void updateUserTimeSlots() {
        User.getUser(this, new User.OnUserListener() {
            @Override
            public void onUser(User user) {
                if (!user.getBookedSlots().containsKey(selected_date)){
                    user.getBookedSlots().put(selected_date, new ArrayList<String>());
                }
                user.getBookedSlots().get(selected_date).add(selected_time);
                FirebaseFirestore.getInstance()
                        .collection("Users")
                        .document(user.getUid())
                        .set(user)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    isUserTimeSlotUpdated = true;
                                    updateUI();
                                } else {
                                    Toast.makeText(Checkout.this, "Some error occurred while updating user time slots: "
                                            + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void updateUI() {
        if (isUserTimeSlotUpdated)
            startNewActivity();
    }


    private void startNewActivity(){
        Intent intent = new Intent(getApplicationContext(), Invoice.class);


        intent.putExtra("BRANCH_NAME", branch_name);
        intent.putParcelableArrayListExtra("SELECTED_SERVICES_LIST",selectedServiceArrayList);
        intent.putExtra("SELECTED_SERVICES_COST", calculateTotal());
        intent.putExtra("SELECTED_DATE", selected_date);
        intent.putExtra("SELECTED_TIME", selected_time);

        startActivity(intent);
        this.finish();
    }

    int calculateTotal(){
        int total=0;
        for (Service service:selectedServiceArrayList) {
            total += service.getServiceCost();
        }
        return total;
    }

    @Override
    public void onItemClick(Service service) {


    }
}