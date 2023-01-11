package com.jaya.financia.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseUser;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.databinding.ActivityEditProfileBinding;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;

    private FirebaseAuth mAuth;
    private String user_uid;
    private FirebaseUser currentUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private String url, xImage, yImage;
    private int xId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        user_uid = mAuth.getUid();
        currentUser = mAuth.getCurrentUser();

        getSupportActionBar().setTitle("Change Your Profile");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        xId = bundle.getInt("xId");
        xImage = bundle.getString("xImage");
        user_uid = bundle.getString("user_uid");

        binding.civProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImageIntent, 1);
            }
        });

        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.civProfileImage.setDrawingCacheEnabled(true);
                binding.civProfileImage.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) binding.civProfileImage.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                // Menyimpan gambar ke Firebase Storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imageRef = storageRef.child(System.currentTimeMillis() +".jpeg");
                UploadTask uploadTask = imageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Gagal menyimpan gambar ke Firebase Storage
                        Toast.makeText(EditProfileActivity.this, "Error " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Gambar berhasil disimpan ke Firebase Storage
                        yImage = imageRef.toString();
                        updateImage();

                        Toast.makeText(EditProfileActivity.this, "Succes Add Profile", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            // Set image to CircleImageView
            Glide.with(EditProfileActivity.this).load(selectedImageUri).into(binding.civProfileImage);

            // Upload image to Firebase Storage
            // ...
        }
    }

    private void updateImage() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseUser> updateImage = api.ardUpdateImage(xId, yImage);

        updateImage.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if (kode == 1) {
                        Toast.makeText(EditProfileActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditProfileActivity.this, pesan, Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(EditProfileActivity.this, SettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_uid", user_uid);
                    intent.putExtra("data", bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(EditProfileActivity.this, "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}