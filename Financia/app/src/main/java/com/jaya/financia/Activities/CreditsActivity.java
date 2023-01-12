package com.jaya.financia.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityCreditsBinding;

public class CreditsActivity extends AppCompatActivity {
    ActivityCreditsBinding binding;
    private FirebaseAuth mAuth;
    private String user_uid;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();
        currentUser = mAuth.getCurrentUser();

        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}