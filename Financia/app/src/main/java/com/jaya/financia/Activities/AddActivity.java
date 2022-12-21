package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.databinding.ActivityAddBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    private String name, type, total, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = binding.etName.getText().toString();
                type = binding.etType.getText().toString();
                total = binding.etTotal.getText().toString();
                date = binding.etDate.getText().toString();

                if (name.trim().equals("")) {
                    binding.etName.setError("Nama harus diisi");
                } else if (type.trim().equals("")) {
                    binding.etType.setError("Tipe harus diisi");
                } else if (total.trim().equals("")) {
                    binding.etTotal.setError("Total harus diisi");
                } else if (date.trim().equals("")) {
                    binding.etDate.setError("Tanggal harus diisi");
                } else {
                    createData();
                }
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> buatData = api.ardCreateData(name, type, total, date);

        buatData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if(kode == 1) {
                        Toast.makeText(AddActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "Response code : " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}