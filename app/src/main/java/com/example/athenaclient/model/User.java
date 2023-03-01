package com.example.athenaclient.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    private static User mUser;
    public interface OnUserListener{
        void onUser(User user);
    }
    public static void getUser(Context context, OnUserListener listener){
        if (mUser != null){
            listener.onUser(mUser);
            return;
        }

        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() == null || !task.getResult().exists()){
                                mUser = new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                        FirebaseAuth.getInstance().getCurrentUser().getEmail(), new HashMap<>());
                                FirebaseFirestore.getInstance()
                                        .collection("Users")
                                        .document(mUser.getUid())
                                        .set(mUser);
                            } else {
                                mUser = task.getResult().toObject(User.class);
                                listener.onUser(mUser);
                            }
                        } else {
                            Toast.makeText(context, "Some error occurred while fetching User Bookings: "
                                    + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    String uid;
    String email;
    HashMap<String, ArrayList<String>> bookedSlots;

    public User() {
    }

    public String getUid() {
        if (uid == null)
            uid = "null";
        return uid;
    }

    public String getEmail() {
        if (email == null)
            email = "null@null.com";
        return email;
    }

    public HashMap<String, ArrayList<String>> getBookedSlots() {
        if (bookedSlots == null)
            bookedSlots = new HashMap<>();
        return bookedSlots;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBookedSlots(HashMap<String, ArrayList<String>> bookedSlots) {
        this.bookedSlots = bookedSlots;
    }

    public User(String uid, String email, HashMap<String, ArrayList<String>> bookedSlots) {
        this.uid = uid;
        this.email = email;
        this.bookedSlots = bookedSlots;
    }
}
