package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Adapter.DataAdapter;
import com.jaya.financia.Model.DataModel;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private RecyclerView.Adapter adapData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private String user_uid;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();

        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Jika sudah login, tampilkan pesan selamat datang dan email pengguna
            SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String email = sharedPref.getString("user_email", "");
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        binding.tvFullName.setText(user.getFullname());
        lmData = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(lmData);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isFirstLogin = sharedPref.getBoolean("first_login", true);
        if (isFirstLogin) {
            // User baru, tampilkan pesan
            Toast.makeText(MainActivity.this, "Welcome, " + mAuth.getCurrentUser().getEmail() + "!", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("first_login", false);
            editor.apply();
        } else {
            // User sudah pernah login, tampilkan pesan selamat datang
            retrieveData();
        }

        retrieveData();

        //getWindow().setNavigationBarColor(SurfaceColors.SURFACE_2.getColor(this));

        binding.fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_uid", user_uid);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        binding.btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, binding.btnFilter);
                popupMenu.getMenuInflater().inflate(R.menu.filter_data_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.sort_latest_date:
                                retrieveFilterDateDesc();
                                binding.btnFilter.setText("Latest date");
                                return true;
                            case R.id.sort_oldest_date:
                                retrieveFilterDate();
                                binding.btnFilter.setText("Oldest date");
                                return true;
                            case R.id.show_only_expenses:
                                type = "expenses";
                                retrieveFilter();
                                binding.btnFilter.setText("Expenses");
                                return true;
                            case R.id.show_only_incomes:
                                type = "incomes";
                                retrieveFilter();
                                binding.btnFilter.setText("Incomes");
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void retrieveData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilData = api.ardRetrieveData(user_uid);

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listData = response.body().getData();

                if (kode == 1) {
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilter() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.llNoData.setVisibility(View.GONE);
        binding.rvData.setVisibility(View.GONE);

        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilter = api.ardDataFilter(type, user_uid);

        tampilDataFilter.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.rvData.setVisibility(View.VISIBLE);
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listData = response.body().getData();

                if (kode == 1) {
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilterDate() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.llNoData.setVisibility(View.GONE);
        binding.rvData.setVisibility(View.GONE);

        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilterDate = api.ardDataFilterDateAsc(user_uid);

        tampilDataFilterDate.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.rvData.setVisibility(View.VISIBLE);
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listData = response.body().getData();

                if (kode == 1) {
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilterDateDesc() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilterDateDesc = api.ardDataFilterDateDesc(user_uid);

        tampilDataFilterDateDesc.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listData = response.body().getData();

                if (kode == 1) {
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    // Data tidak ditemukan
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
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
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }
}