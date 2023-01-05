package com.jaya.financia.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Adapter.DataAdapter;
import com.jaya.financia.Model.DataModel;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityMainBinding;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements DataAdapter.OnItemLongClickListener {
    ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private RecyclerView.Adapter adapData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private List<DataModel> listNote = new ArrayList<>();
    private List<UserModel> listUser = new ArrayList<>();
    private String user_uid;
    private String type;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();
        UserModel userModel = new UserModel();

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

        lmData = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(lmData);

        binding.progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isFirstLogin = sharedPref.getBoolean("first_login", true);
        if (isFirstLogin) {
            // User baru, tampilkan pesan
            userModel.getUser_uid();
            Toast.makeText(MainActivity.this, "Welcome, " + userModel.getName() + "!", Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("first_login", false);
            editor.apply();
        } else {
            // User sudah pernah login, tampilkan pesan selamat datang
            retrieveData();
            retrieveTotal();
        }

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveData();
                retrieveFilter();
                retrieveFilterDate();
                retrieveFilterDateDesc();
            }
        });

        getFullName();

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
                binding.progressBar.setVisibility(View.VISIBLE);
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

    public void getFullName() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> tampilUser = api.ardGetUser(user_uid);
        tampilUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                binding.progressBar.setVisibility(View.GONE);
                int kode = response.body().getKode();
                UserModel user = new UserModel();
                user = response.body().getData().get(0);

                if(kode==1) {
                    binding.progressBar.setVisibility(View.GONE);
                    binding.tvFullName.setText(user.getName());
                }else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Kode : " + kode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error : " +t.getMessage() , Toast.LENGTH_SHORT).show();
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
//                int total = response.body().getTotal();
                String pesan = response.body().getPesan();
//                binding.tvAmount.setText(String.valueOf(total));
                listData = response.body().getData();

                if (kode == 1) {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveTotal() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilTotal = api.ardRetrieveTotal(user_uid);

        tampilTotal.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.rvData.setVisibility(View.VISIBLE);
                int kode = response.body().getKode();
                int total = response.body().getTotal();
                DecimalFormat df = new DecimalFormat("#,###.##");
                DecimalFormatSymbols symbols = df.getDecimalFormatSymbols();
                symbols.setGroupingSeparator('.');
                df.setDecimalFormatSymbols(symbols);
                String formatted = df.format(total);
                String Tot = String.format(formatted);

                if (kode == 1) {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data ditemukan
                    binding.tvAmount.setText("Rp" + Tot);
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilter() {
        binding.swipeRefresh.setRefreshing(true);
        binding.llNoData.setVisibility(View.GONE);
        binding.rvData.setVisibility(View.GONE);

        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilter = api.ardDataFilter(type, user_uid);

        tampilDataFilter.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.rvData.setVisibility(View.VISIBLE);
                int kode = response.body().getKode();
                listData = response.body().getData();

                if (kode == 1) {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilterDate() {
        binding.swipeRefresh.setRefreshing(true);
        binding.llNoData.setVisibility(View.GONE);
        binding.rvData.setVisibility(View.GONE);

        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilterDate = api.ardDataFilterDateAsc(user_uid);

        tampilDataFilterDate.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                binding.rvData.setVisibility(View.VISIBLE);
                int kode = response.body().getKode();
                listData = response.body().getData();

                if (kode == 1) {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data tidak ditemukan
                    binding.llNoData.setVisibility(View.VISIBLE);
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void retrieveFilterDateDesc() {
        binding.swipeRefresh.setRefreshing(true);
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilDataFilterDateDesc = api.ardDataFilterDateDesc(user_uid);

        tampilDataFilterDateDesc.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                listData = response.body().getData();

                if (kode == 1) {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data ditemukan
                    adapData = new DataAdapter(listData, MainActivity.this, MainActivity.this);
                    binding.rvData.setAdapter(adapData);
                    adapData.notifyDataSetChanged();
                } else {
                    binding.swipeRefresh.setRefreshing(false);
                    // Data tidak ditemukan
                    binding.rvData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemLongClick(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
        popupMenu.inflate(R.menu.menu_popup);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            popupMenu.setGravity(Gravity.RIGHT);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_update:
                        id = listData.get(position).getId();
                        type = listData.get(position).getType();
                        getData();
                        return true;
                    case R.id.action_delete:
                        id = listData.get(position).getId();
                        type = listData.get(position).getType();
                        MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(MainActivity.this)
                                .setTitle("Confirm")
                                .setMessage("Are you sure delete data '" + listData.get(position).getNote() + "'?")
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                })
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteData();
                                    }
                                });
                        alert.create();
                        alert.show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
//        Toast.makeText(this, "Item Position Click : " + position, Toast.LENGTH_SHORT).show();
    }

    private void deleteData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> deleteData = api.ardDeleteData(type, id);

        deleteData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 200) {
                    binding.swipeRefresh.setRefreshing(false);
                    int kode = response.body().getKode();

                    if (kode == 1) {
                        retrieveData();
                        retrieveFilter();
                        retrieveFilterDate();
                        retrieveFilterDateDesc();
                        retrieveTotal();
                    } else {
                        binding.swipeRefresh.setRefreshing(false);
                    }
                } else {
                    binding.swipeRefresh.setRefreshing(false);
//                    Toast.makeText(MainActivity.this, "kode : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                binding.swipeRefresh.setRefreshing(false);
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> getData = api.ardGetData(type, id);

        getData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                listNote = response.body().getData();

                int varId = listNote.get(0).getId();
                String varNote = listNote.get(0).getNote();
                String varCat = listNote.get(0).getCategory();
                String varAmount = listNote.get(0).getAmount();
                String varType = listNote.get(0).getType();
                String varDate = listNote.get(0).getDate();

                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("xId", varId);
                bundle.putString("xNote", varNote);
                bundle.putString("xCat", varCat);
                bundle.putString("xAmount", varAmount);
                bundle.putString("xType", varType);
                bundle.putString("xDate", varDate);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Gagal terhubung ke server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}