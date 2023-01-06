package com.jaya.financia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.databinding.FragmentExpensesBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpensesFragment extends Fragment {
    FragmentExpensesBinding binding;
    private String type, category, note, amount, date, user_uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentExpensesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
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
                if (materialDatePicker.isAdded()) {
                    return;
                } else {
                    materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
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
                type = "expenses";
                category = binding.etCategory.getEditText().getText().toString();
                note = binding.etNote.getEditText().getText().toString();
                amount = binding.etAmount.getEditText().getText().toString();

                // Cek apakah semua input telah diisi
                if (note.isEmpty() || type.isEmpty() || amount.isEmpty()) {
                    // Tampilkan pesan error jika ada input yang belum diisi
                    if (category.isEmpty()) {
                        binding.etCategory.setError("What is it?");
                    }
                    if (note.isEmpty()) {
                        binding.etNote.setError("Tell us the details.");
                    }
                    if (amount.isEmpty()) {
                        binding.etAmount.setError("How much is it?");
                    }
                    Toast.makeText(getActivity(), "Please fill all the information.", Toast.LENGTH_SHORT).show();
                } else {
                    createData();
                }
            }
        });
        return view;
    }
    private void createData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> buatData = api.ardCreateData(type, category, note, amount, date, user_uid);

        buatData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if (kode == 1) {
                        binding.etCategory.getEditText().setText("");
                        binding.etNote.getEditText().setText("");
                        binding.etAmount.getEditText().setText("");
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), pesan + " " + category + type, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
