package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityRegisterBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRoot, mRef;
    private String fullname, email, user_uid, imageUrl;
    private List<UserModel> listUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRoot = mDatabase.getReference();

        binding.progressBar.setVisibility(View.GONE);

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                String confirmPassword = binding.etConfirmPassword.getText().toString();
                String fullname = binding.etFullname.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Enter your email address!");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    binding.etPassword.setError("Enter your password!");
                    return;
                }
                if (TextUtils.isEmpty(confirmPassword)) {
                    binding.etConfirmPassword.setError("Enter your confirm password!");
                    return;
                }
                if (TextUtils.isEmpty(fullname)) {
                    binding.etFullname.setError("Enter your full name!");
                    return;
                }
                if (password.length() < 6) {
                    binding.etPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }
                if (!confirmPassword.equals(password)) {
                    binding.etConfirmPassword.setError("Password doesn't match!");
                    return;
                }

                binding.progressBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                binding.progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
                                    UserModel user = new UserModel(fullname, email, user_uid, imageUrl);
                                    String userId = task.getResult().getUser().getUid();
                                    createUser(user.getName(), user.getEmail(), userId);
//                                    String userId = mAuth.getCurrentUser().getUid();
                                    mRef = mRoot.child("users").child(userId);
                                    mRef.setValue(user);
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity

                                            .class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createUser(String fullname, String email, String user_uid) {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> buatUser = api.ardCreateUser(fullname, email, user_uid);

        buatUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}