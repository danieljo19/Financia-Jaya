package com.jaya.financia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivitySettingBinding;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();

        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Settings");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user_uid = bundle.getString("user_uid");

        getFullName();

        binding.bottomNavigation.setSelectedItemId(R.id.item_3);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item_1:
                        Intent intentMain = new Intent(SettingActivity.this, MainActivity.class);
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("user_uid", user_uid);
                        intentMain.putExtra("data", bundleMain);
                        startActivity(intentMain);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.item_2:
                        Intent intentAnalytic = new Intent(SettingActivity.this, AnalyticActivity.class);
                        Bundle bundleAnalytic = new Bundle();
                        bundleAnalytic.putString("user_uid", user_uid);
                        intentAnalytic.putExtra("data", bundleAnalytic);
                        startActivity(intentAnalytic);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.item_3:
                        return true;
                }
                return false;
            }
        });

        binding.tvAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUser();
            }
        });

        binding.tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_uid", user_uid);
                intent.putExtra("data", bundle);
                startActivity(intent);
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
                Toast.makeText(SettingActivity.this, "Error Name: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(SettingActivity.this, EditNameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("xId", varId);
                bundle.putString("xName", varName);
                bundle.putString("user_uid", user_uid);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                //                Toast.makeText(MainActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}