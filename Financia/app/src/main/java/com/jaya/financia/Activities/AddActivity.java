package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
import com.jaya.financia.databinding.ActivityAddBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        //Inisialisasi kalender
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Select date");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

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

                int year = binding.datePicker.getYear();
                int month = binding.datePicker.getMonth();
                int day = binding.datePicker.getDayOfMonth();

                Calendar calendar = Calendar.getInstance();
                calendar.set(day, month, year);
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                date = sdf.format(calendar.getTime());

                // Cek apakah semua input telah diisi
                if (name.isEmpty() && type.isEmpty() && total.isEmpty()) {
                    // Tampilkan pesan error jika ada input yang belum diisi
                    Toast.makeText(AddActivity.this, "Semua input harus diisi!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    createData();
                }

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