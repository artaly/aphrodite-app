package com.example.athenaclient.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.athenaclient.BranchInterface;
import com.example.athenaclient.Home;
import com.example.athenaclient.R;
import com.example.athenaclient.adapter.BranchAdapter;
import com.example.athenaclient.helpers.BranchSearchHelpers;
import com.example.athenaclient.model.Branch;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BranchInterface {

    RecyclerView branchRecyclerView;
    ArrayList<Branch> branchArrayList;
    BranchAdapter branchAdapter;
    FirebaseFirestore db;
    BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        branchRecyclerView = findViewById(R.id.branchRecycler);
        branchRecyclerView.setHasFixedSize(true);
        branchRecyclerView.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        branchArrayList = new ArrayList<>();
        branchAdapter = new BranchAdapter(MainActivity.this, branchArrayList, this);

        branchRecyclerView.setAdapter(branchAdapter);

        EventListener();
        setSearchListener();

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

    private void setSearchListener() {
        EditText branchSearchEt = findViewById(R.id.branchSearchBar);
        branchSearchEt.setText("");
        findViewById(R.id.branchSearchButton).setOnClickListener(view -> {
            try {
                View active = MainActivity.this.getCurrentFocus();
                if (active != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(active.getWindowToken(), 0);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
        BranchSearchHelpers.implementBranchSearch(branchSearchEt, branchAdapter, branchArrayList);
    }

    private void EventListener() {
        db.collection("branches").orderBy("branchName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                branchArrayList.add(dc.getDocument().toObject(Branch.class));

                            }

                            branchAdapter.notifyDataSetChanged();
                        }
                        setSearchListener();

                    }
                });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), BookBranch.class);

        intent.putExtra("BRANCH_NAME", branchArrayList.get(position).getBranchName());
        intent.putExtra("BRANCH_HOURS", branchArrayList.get(position).getBranchHours());
        intent.putExtra("BRANCH_LOCATION", branchArrayList.get(position).getBranchLocation());
        intent.putExtra("BRANCH_PHOTO", branchArrayList.get(position).getBranchPhoto());

        startActivity(intent);


    }
}