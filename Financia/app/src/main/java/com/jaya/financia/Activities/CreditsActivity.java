package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityChangePasswordBinding;
import com.jaya.financia.databinding.ActivityCreditsBinding;

import maes.tech.intentanim.CustomIntent;

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
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());

        binding = ActivityCreditsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setSelectedItemId(R.id.item_3);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_1:
                        Intent intentMain = new Intent(CreditsActivity.this, MainActivity.class);
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("user_uid", user_uid);
                        intentMain.putExtra("data", bundleMain);
                        startActivity(intentMain);
                        CustomIntent.customType(CreditsActivity.this, "right-to-left");
                        return true;
                    case R.id.item_2:
                        Intent intentAnalytic = new Intent(CreditsActivity.this, AnalyticActivity.class);
                        Bundle bundleAnalytic = new Bundle();
                        bundleAnalytic.putString("user_uid", user_uid);
                        intentAnalytic.putExtra("data", bundleAnalytic);
                        startActivity(intentAnalytic);
                        CustomIntent.customType(CreditsActivity.this, "right-to-left");
                        return true;
                    case R.id.item_3:
                        return true;
                }
                return false;
            }
        });
    }
}