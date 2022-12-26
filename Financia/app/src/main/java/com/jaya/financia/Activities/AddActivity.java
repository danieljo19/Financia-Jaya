package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.databinding.ActivityAddBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    private String name, type, total, date, user_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("New Transaction");

        Bundle bundle = getIntent().getExtras();
        user_uid = bundle.getString("user_uid");

        // Mendapatkan tanggal saat ini
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        date = format.format(calendar.getTime());

        // Inisialisasi kalender
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(calendar.getTimeInMillis());
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(materialDatePicker.isAdded()) {
                    return;
                } else {
                    materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                }
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.btnDatePicker.setText(materialDatePicker.getHeaderText());
                calendar.setTimeInMillis((Long) selection);
                date = format.format(calendar.getTime());
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = binding.etName.getEditText().getText().toString();
                type = "";
                if(binding.chipIncome.isChecked()) {
                    type = "In";
                } else if(binding.chipOutcome.isChecked()) {
                    type = "Out";
                }
                total = binding.etTotal.getEditText().getText().toString();

                // Cek apakah semua input telah diisi
                if (name.isEmpty() || type.isEmpty() || total.isEmpty()) {
                    // Tampilkan pesan error jika ada input yang belum diisi
                    if (name.isEmpty()) {
                        binding.etName.setError("What is it?");
                    }
                    if (total.isEmpty()) {
                        binding.etTotal.setError("How much is it?");
                    }
                    Toast.makeText(AddActivity.this, "Please fill all the information.", Toast.LENGTH_SHORT).show();
                } else {
                    createData();
                }
            }
        });
    }

    private void createData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> buatData = api.ardCreateData(name, type, total, date, user_uid);

        buatData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if(kode == 1) {
                        binding.etName.getEditText().setText("");
                        binding.etTotal.getEditText().setText("");
                        Toast.makeText(AddActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AddActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}