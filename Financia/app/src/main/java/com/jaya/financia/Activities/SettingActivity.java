package com.jaya.financia.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivitySettingBinding;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity {
    ActivitySettingBinding binding;
    private UserModel userModel;
    private FirebaseAuth mAuth;
    private String user_uid, name;
    private int id;
    private List<UserModel> listUser = new ArrayList<>();
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

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Settings");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user_uid = bundle.getString("user_uid");

        getFullName();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user.getImageUrl() != null) {
                    Glide.with(SettingActivity.this)
                            .load(user.getImageUrl())
                            .into(binding.civProfileImage);
                }
                Toast.makeText(SettingActivity.this, "Berhasil upload", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(SettingActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        binding.bottomNavigation.setSelectedItemId(R.id.item_3);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_1:
                        Intent intentMain = new Intent(SettingActivity.this, MainActivity.class);
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("user_uid", user_uid);
                        intentMain.putExtra("data", bundleMain);
                        startActivity(intentMain);
                        CustomIntent.customType(SettingActivity.this, "right-to-left");
                        return true;
                    case R.id.item_2:
                        Intent intentAnalytic = new Intent(SettingActivity.this, AnalyticActivity.class);
                        Bundle bundleAnalytic = new Bundle();
                        bundleAnalytic.putString("user_uid", user_uid);
                        intentAnalytic.putExtra("data", bundleAnalytic);
                        startActivity(intentAnalytic);
                        CustomIntent.customType(SettingActivity.this, "right-to-left");
                        return true;
                    case R.id.item_3:
                        return true;
                }
                return false;
            }
        });

        binding.llAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });

        binding.llChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_uid", user_uid);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        binding.llChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_uid", user_uid);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        });

        binding.llLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(SettingActivity.this)
                        .setTitle("Get Ready")
                        .setMessage("Something really cool is coming!")
                        .setNegativeButton("Got it", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alert.create();
                alert.show();
            }
        });

        binding.llCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentC = new Intent(SettingActivity.this, CreditsActivity.class);
                startActivity(intentC);
                CustomIntent.customType(SettingActivity.this, "left-to-right");
            }
        });
    }

    public void getFullName() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> tampilUser = api.ardGetUser(user_uid);
        tampilUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                int kode = response.body().getKode();
                userModel = response.body().getData().get(0);

                if (kode == 1) {
                    binding.tvFullName.setText(userModel.getName());
                    binding.tvEmail.setText(userModel.getEmail());
                } else {
                    binding.tvFullName.setText("Error");
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUser() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> getUser = api.ardGetUser(user_uid);

        getUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                listUser = response.body().getData();

                int varId = listUser.get(0).getId();
                String varName = listUser.get(0).getName();
                String varImage = listUser.get(0).getImageUrl();

                Intent intent = new Intent(SettingActivity.this, EditNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("xId", varId);
                bundle.putString("xName", varName);
                bundle.putString("xImage", varImage);
                bundle.putString("user_uid", user_uid);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(SettingActivity.this, "Failed to connect to the server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        mAuth.signOut();
        // Hapus data email dari shared preferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("user_email");
        editor.apply();
        // Buka activity login
        startActivity(new Intent(SettingActivity.this, LoginActivity.class));
        finish();
    }
}