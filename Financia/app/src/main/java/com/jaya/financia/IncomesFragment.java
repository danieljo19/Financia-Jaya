package com.jaya.financia;


import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.databinding.FragmentIncomesBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomesFragment extends Fragment {
    FragmentIncomesBinding binding;
    private String type, category, note, amount, date, user_uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentIncomesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        user_uid = bundle.getString("user_uid");

        binding.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

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
                type = "incomes";
                category = binding.tvCategory.getText().toString();
                note = binding.etNote.getEditText().getText().toString();
                amount = binding.etAmount.getEditText().getText().toString();

                // Cek apakah semua input telah diisi
                if (note.isEmpty() || type.isEmpty() || amount.isEmpty()) {
                    // Tampilkan pesan error jika ada input yang belum diisi
                    if (category.isEmpty()) {
                        binding.tvCategory.setError("What is it?");
                    }
                    if (note.isEmpty()) {
                        binding.etNote.setError("Tell us the details.");
                    }
                    if (amount.isEmpty()) {
                        binding.etAmount.setError("How much is it?");
                    }
                    Toast.makeText(getActivity(), "Please fill all the information.", Toast.LENGTH_SHORT).show();
                } else {
                    // Tampilkan dialog dengan progress bar dan text
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.progress_dialog, null);
                    builder.setView(dialogView);
                    builder.setCancelable(false);
                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    // Tampilkan text pada TextView pada layout progress_dialog
                    TextView tvMessage = dialogView.findViewById(R.id.tv_message);
                    tvMessage.setText("Saving data...");

                    // Buat data setelah dialog ditampilkan
                    createData(dialog);
                }
            }
        });
        return view;
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout2);

        LinearLayout llSalary = dialog.findViewById(R.id.ll_salary);
        LinearLayout llBusiness = dialog.findViewById(R.id.ll_business);
        LinearLayout llGifts = dialog.findViewById(R.id.ll_gifts);
        LinearLayout llExtraIncome = dialog.findViewById(R.id.ll_extraIncome);
        LinearLayout llLoan = dialog.findViewById(R.id.ll_loan);
        LinearLayout llInvestment = dialog.findViewById(R.id.ll_investment);
        LinearLayout llInsurancePayout = dialog.findViewById(R.id.ll_insurancePayout);
        LinearLayout llOther = dialog.findViewById(R.id.ll_other);

        llSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("1");
                dialog.dismiss();
            }
        });

        llBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("2");
                dialog.dismiss();
            }
        });

        llGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("3");
                dialog.dismiss();
            }
        });

        llExtraIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("4");
                dialog.dismiss();
            }
        });

        llLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("5");
                dialog.dismiss();
            }
        });

        llInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("6");
                dialog.dismiss();
            }
        });

        llInsurancePayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("7");
                dialog.dismiss();
            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvCategory.setText("8");
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void createData(final AlertDialog dialog) {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> buatData = api.ardCreateData(type, category, note, amount, date, user_uid);

        buatData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.code() == 200) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    if (kode == 1) {
                        binding.tvCategory.setText("");
                        binding.etNote.getEditText().setText("");
                        binding.etAmount.getEditText().setText("");
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), pesan + " " + category + type, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
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