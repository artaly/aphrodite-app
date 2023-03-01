package com.example.athenaclient.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.athenaclient.fragment.DatePickerFragment;
import com.example.athenaclient.R;
import com.example.athenaclient.fragment.TimePickerFragment;
import com.example.athenaclient.model.Booking;
import com.example.athenaclient.model.Service;
import com.example.athenaclient.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SelectDate extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Button btnSelectDate, btnContinue;
    String TAG="SelectDate";
    Calendar today =  Calendar.getInstance();
    DatePicker datePicker;
    TextView lblChosenDate, lblDate, lblChosenTime, lblTime,lblSelectDateNew, lblSelectTimeNew, txtChooseTime;
    String selectedTime="";
    String selectedDate="";
    ArrayList<Service> selectedServiceArrayList;
    String branch_name="";
    ImageView backButton;
    ProgressBar progressBar;
    Spinner pickTimeSlot;

    User mUser;

    private void setAvailableTimes(){
        progressBar.setVisibility(View.GONE);
        ArrayList<String> timeSlots = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.available_time)));
        filterAvailableTimes(timeSlots);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeSlots);
        pickTimeSlot.setAdapter(adapter);
        pickTimeSlot.setVisibility(View.VISIBLE);
        pickTimeSlot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = pickTimeSlot.getSelectedItem().toString();
                lblTime.setVisibility(View.GONE);
                lblChosenTime.setVisibility(View.VISIBLE);
                lblChosenTime.setText(selectedTime);
                lblSelectTimeNew.setVisibility(View.GONE);

                txtChooseTime.setVisibility(View.VISIBLE);
                pickTimeSlot.setVisibility(View.VISIBLE);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void filterAvailableTimes(ArrayList<String> timeSlots) {
        if (mUser != null
        && mUser.getBookedSlots() != null
        && mUser.getBookedSlots().containsKey(selectedDate)) {
            ArrayList<String> times = mUser.getBookedSlots().get(selectedDate);
            for (String time : times) {
                if (timeSlots.contains(time)) {
                    timeSlots.remove(time);
                }
            }
        }
        for (Booking booking: bookings){
            if (timeSlots.contains(booking.getTime())){
                timeSlots.remove(booking.getTime());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        selectedServiceArrayList=new ArrayList<Service>();
        if(getIntent()!=null){
            branch_name =  getIntent().getStringExtra("BRANCH_NAME");
            selectedServiceArrayList=getIntent().getParcelableArrayListExtra("SELECTED_SERVICES_LIST");
        }
        Log.d(TAG,"Selectedlist Size is "+selectedServiceArrayList.size());

        pickTimeSlot = findViewById(R.id.time_spinner);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        pickTimeSlot.setVisibility(View.GONE);


        btnSelectDate = findViewById(R.id.btnSetDate);
        btnContinue = findViewById(R.id.btnContinue);
        backButton = findViewById(R.id.backButton);
        lblChosenDate = findViewById(R.id.lblChosenDate);
        lblDate = findViewById(R.id.lblDate);
        lblTime = findViewById(R.id.lblTime);
        txtChooseTime = findViewById(R.id.txtChooseTime);
        lblChosenTime = findViewById(R.id.lblChosenTime);
        lblSelectDateNew = findViewById(R.id.lblSelectDateNew);
        lblSelectTimeNew = findViewById(R.id.lblSelectTimeNew);

        lblDate.setVisibility(View.GONE);
        lblChosenDate.setVisibility(View.GONE);

        lblTime.setVisibility(View.GONE);
        lblChosenTime.setVisibility(View.GONE);
        lblSelectDateNew.setVisibility(View.GONE);
        lblSelectTimeNew.setVisibility(View.GONE);


        btnSelectDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");
        });

        backButton.setOnClickListener(view -> {
            super.onBackPressed();
        });

        lblSelectDateNew.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "date picker");

        });

        lblSelectTimeNew.setOnClickListener(view -> {

        });


        btnContinue.setOnClickListener(view -> {
            if (selectedDate.isEmpty()){
                Toast.makeText(this, "Please select Date.", Toast.LENGTH_SHORT).show();
            }else if(selectedTime.isEmpty()){
                Toast.makeText(this, "Please select Time.", Toast.LENGTH_SHORT).show();
            }
            else {
                Intent intent = new Intent(getApplicationContext(), Checkout.class);
                intent.putExtra("BRANCH_NAME", branch_name);
                intent.putParcelableArrayListExtra("SELECTED_SERVICES_LIST",selectedServiceArrayList);
                intent.putExtra("SELECTED_DATE", selectedDate);
                intent.putExtra("SELECTED_TIME", selectedTime);
                startActivity(intent);
            }
        });

        User.getUser(this, new User.OnUserListener() {
            @Override
            public void onUser(User user) {
                mUser = user;
                updateUI();
            }
        });

    }

    private void updateUI(){
        if (mUser != null && timeForBranchLoaded){
            setAvailableTimes();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        lblChosenDate.setText(currentDateString);
        selectedDate = currentDateString;

        lblDate.setVisibility(View.VISIBLE);
        lblChosenDate.setVisibility(View.VISIBLE);
        btnSelectDate.setVisibility(View.GONE);

        lblSelectDateNew.setVisibility(View.VISIBLE);

        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Loading available times", Toast.LENGTH_SHORT).show();
        loadAvailableTimeForBranch();
    }

    ArrayList<Booking> bookings;
    boolean timeForBranchLoaded = false;
    private void loadAvailableTimeForBranch() {
        timeForBranchLoaded = false;
        FirebaseFirestore.getInstance()
                .collection("bookings")
                .whereEqualTo("branch", branch_name)
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        timeForBranchLoaded = true;
                        bookings = new ArrayList<Booking>();
                        if (task.isSuccessful()){
                            if (!task.getResult().isEmpty()) {
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                    Booking booking = documentSnapshot.toObject(Booking.class);
                                    bookings.add(booking);
                                }
                            }
                            updateUI();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SelectDate.this, "Error while loading bookings for selected date: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}