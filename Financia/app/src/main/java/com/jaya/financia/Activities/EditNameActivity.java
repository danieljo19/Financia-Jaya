package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityEditNameBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditNameActivity extends AppCompatActivity {
    ActivityEditNameBinding binding;
    private FirebaseAuth mAuth;
    private String user_uid;
    private String xName, yName;
    private int xId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();

        binding = ActivityEditNameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Edit Your Name");

        Bundle bundle = getIntent().getBundleExtra("data");
        xId = bundle.getInt("xId");
        xName = bundle.getString("xName");
        user_uid = bundle.getString("user_uid");


        binding.tvEdit.setText("Hello, " + xName + "!");
        binding.etNama.getEditText().setText(xName);
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yName = binding.etNama.getEditText().getText().toString();

                updateUser();
            }
        });
    }

    private void updateUser() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> updateUser = api.ardUpdateUser(xId, yName);

        updateUser.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if (kode == 1) {
                        Toast.makeText(EditNameActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditNameActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(EditNameActivity.this, SettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_uid", user_uid);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditNameActivity.this, "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(EditNameActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}