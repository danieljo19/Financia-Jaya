package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaya.financia.databinding.ActivityLoginBinding;

import maes.tech.intentanim.CustomIntent;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRoot, mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRoot = mDatabase.getReference();

        // Cek apakah user sudah login sebelumnya
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        binding.progressBar.setVisibility(View.GONE);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Pengguna sedang login, tampilkan MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    // Pengguna belum login, tampilkan layar login
                    //Toast.makeText(LoginActivity.this, "Log out succesful!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                if(TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Email is required");
                    binding.etEmail.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    binding.etPassword.setError("Password is required");
                    binding.etPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    binding.etPassword.setError("Minimum length of password should be 6");
                    binding.etPassword.requestFocus();
                    return;
                }

                binding.progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if (task.isSuccessful()) {
                        // Login berhasil, simpan email ke shared preferences
                        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user_email", email);
                        editor.apply();

                        // Buka activity utama
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        CustomIntent.customType(LoginActivity.this, "left-to-right");
                        finish();
                    } else {
                        // Login gagal, tampilkan pesan error
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                CustomIntent.customType(LoginActivity.this, "left-to-right");
                finish();
            }
        });

        binding.btnResetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                CustomIntent.customType(LoginActivity.this, "left-to-right");
                finish();
            }
        });
    }
}