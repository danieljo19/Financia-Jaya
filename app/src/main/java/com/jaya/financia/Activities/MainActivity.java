package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Adapter.DataAdapter;
import com.jaya.financia.Model.DataModel;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        lmData = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvData.setLayoutManager(lmData);
        retrieveData();
    }

    public void retrieveData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilData = api.ardRetrieveData();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(MainActivity.this, "Kode : " + kode +" | Pesan " + pesan, Toast.LENGTH_SHORT).show();

                listData = response.body().getData();

                adapData = new DataAdapter(listData, MainActivity.this);
                binding.rvData.setAdapter(adapData);
                adapData.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi ke Server", Toast.LENGTH_SHORT).show();
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
        if(id == R.id.action_logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}