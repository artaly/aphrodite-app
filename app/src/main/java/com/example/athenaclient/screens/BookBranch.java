package com.example.athenaclient.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.athenaclient.R;

public class BookBranch extends AppCompatActivity {

    private static final String TAG = "Photo URL: ";

    TextView branchName, branchHours, branchLocation;
    ImageView branchPhoto, backButton;
    Button btnBookBranch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_branch);

        String branch_name =  getIntent().getStringExtra("BRANCH_NAME");
        String branch_hours =  getIntent().getStringExtra("BRANCH_HOURS");
        String branch_location =  getIntent().getStringExtra("BRANCH_LOCATION");
        String image = getIntent().getStringExtra("BRANCH_PHOTO");

        btnBookBranch = findViewById(R.id.btnBookBranch);
        branchHours = findViewById(R.id.branchHours);
        branchName = findViewById(R.id.branchName);
        branchLocation = findViewById(R.id.branchLocation);
        branchPhoto = findViewById(R.id.branchPhoto);
        backButton = findViewById(R.id.backButton);

        Log.d(TAG, image);

        branchHours.setText(branch_hours);
        branchName.setText(branch_name);
        branchLocation.setText(branch_location);

        Glide.with(this)
                .load(image)
                .into(branchPhoto);

        backButton.setOnClickListener(view -> {
            super.onBackPressed();
        });


        btnBookBranch.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SelectService.class);
            intent.putExtra("BRANCH_NAME", branch_name);
            startActivity(intent);

        });
    }
}