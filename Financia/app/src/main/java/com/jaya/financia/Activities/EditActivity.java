package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
import com.jaya.financia.databinding.ActivityAddBinding;
import com.jaya.financia.databinding.ActivityEditBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    ActivityEditBinding binding;
    private int xId;
    private String xType, date;
    private String xNote, xCat, xAmount, xDate;
    private String yNote, yCat, yAmount, yDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Edit Transaction");

        Intent intent = getIntent();
        xId = intent.getIntExtra("xId", -1);
        xType = intent.getStringExtra("xType");
        xCat = intent.getStringExtra("xCat");
        xNote = intent.getStringExtra("xNote");
        xAmount = intent.getStringExtra("xAmount");
        xDate = intent.getStringExtra("xDate");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatButton = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        yDate = xDate;
        Date dateN = new Date();
        try {
            dateN = format.parse(xDate);
            date = formatButton.format(dateN);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (xType.equalsIgnoreCase("expenses")) {
            binding.tvEdit.setText("Edit expenses");
        } else {
            binding.tvEdit.setText("Edit incomes");
        }
        binding.etCategory.getEditText().setText(xCat);
        binding.etNote.getEditText().setText(xNote);
        binding.etAmount.getEditText().setText(xAmount);
        binding.btnDatePicker.setText(date);

        // Inisialisasi kalender
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(dateN.getTime());
        final MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();

        binding.btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (materialDatePicker.isAdded()) {
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
                yDate = format.format(calendar.getTime());
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNote = binding.etNote.getEditText().getText().toString();
                yCat = binding.etCategory.getEditText().getText().toString();
                yAmount = binding.etAmount.getEditText().getText().toString();

                updateData();
            }
        });
    }

    private void updateData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> updateData = api.ardUpdateData(xId, xType, yCat, yNote, yAmount, yDate);

        updateData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if (kode == 1) {
                        Toast.makeText(EditActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditActivity.this, "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(EditActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(EditActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}