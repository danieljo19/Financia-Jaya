package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
import com.jaya.financia.databinding.ActivityEditBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {
    private int xId;
    private String xNote, xCat, xAmount, xType, xDate;
    private TextInputEditText note, cat, amount, type, date;
    private Button btnEdit;
    private String yNote, yCat, yAmount, yType, yDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Receive the data passed through the Intent
        Intent intent = getIntent();
        xId = intent.getIntExtra("xId", -1);
        xNote = intent.getStringExtra("xNote");
        xCat = intent.getStringExtra("xCat");
        xAmount = intent.getStringExtra("xAmount");
        xType = intent.getStringExtra("xType");
        xDate = intent.getStringExtra("xDate");

        // Initialize the TextInputEditText widgets
        note = findViewById(R.id.et_note);
        cat = findViewById(R.id.et_category);
        amount = findViewById(R.id.et_amount);
        type = findViewById(R.id.et_type);
        date = findViewById(R.id.et_date);
        btnEdit = findViewById(R.id.btn_edit);

        // Set the data received through the Intent to the TextInputEditText widgets
        note.setText(xNote);
        cat.setText(xCat);
        amount.setText(xAmount);
        type.setText(xType);
        date.setText(xDate);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yNote = note.getText().toString();
                yCat = cat.getText().toString();
                yAmount = amount.getText().toString();
                yType = type.getText().toString();
                yDate = date.getText().toString();

                updateData();
            }
        });
    }

    private void updateData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> updateData = api.ardUpdateData(xId, yType, yCat, yNote, yAmount, yDate);

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
}