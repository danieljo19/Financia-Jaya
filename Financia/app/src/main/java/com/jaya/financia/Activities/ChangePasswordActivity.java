package com.jaya.financia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String user_uid;
    private String oldPass, newPass, confirmNewPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();

        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            ((FirebaseUser) mUser).getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                String idToken = task.getResult().getToken();
                                // Simpan token dan periksa kembali apakah masih berlaku setiap kali Anda melakukan operasi yang membutuhkan autentikasi terbaru
//                                Toast.makeText(ChangePasswordActivity.this, idToken, Toast.LENGTH_SHORT).show();
                            } else {
                                // Gagal mendapatkan token
                            }
                        }
                    });
        } else {
            // Pengguna belum terautentikasi
        }

        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Change Password");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user_uid = bundle.getString("user_uid");

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPass = binding.etPassword.getText().toString();
                newPass = binding.etNewPassword.getText().toString();
                confirmNewPass = binding.etConfirmNewPassword.getText().toString();

                if (TextUtils.isEmpty(oldPass)) {
                    binding.etPassword.setError("Enter your password!");
                    return;
                }

                if (TextUtils.isEmpty(newPass)) {
                    binding.etNewPassword.setError("Enter your new password!");
                    return;
                }

                if (TextUtils.isEmpty(confirmNewPass)) {
                    binding.etConfirmNewPassword.setError("Enter your confirm new password!");
                    return;
                }

                if (oldPass.length() < 6) {
                    binding.etPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                if (newPass.length() < 6) {
                    binding.etNewPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                if (confirmNewPass.length() < 6) {
                    binding.etConfirmNewPassword.setError("Password too short, enter minimum 6 characters!");
                    return;
                }

                if (!confirmNewPass.equals(newPass)) {
                    binding.etConfirmNewPassword.setError("Password doesn't match!");
                    return;
                }

                mUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent1 = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                            startActivity(intent1);
                            finish();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ChangePasswordActivity.this, "Error : " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
                Bundle bundleSettings = new Bundle();
                bundleSettings.putString("user_uid", user_uid);
                intent.putExtra("data", bundleSettings);
                startActivity(intent);
                finish();
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
        Intent intent = new Intent(ChangePasswordActivity.this, SettingActivity.class);
        Bundle bundleSettings = new Bundle();
        bundleSettings.putString("user_uid", user_uid);
        intent.putExtra("data", bundleSettings);
        startActivity(intent);
        finish();
    }
}