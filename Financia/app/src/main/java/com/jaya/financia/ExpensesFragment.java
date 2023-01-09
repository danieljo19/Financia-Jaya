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
                type = "expenses";
                note = binding.etNote.getEditText().getText().toString();
                amount = binding.etAmount.getEditText().getText().toString();

                // Cek apakah semua input telah diisi
                if (note.isEmpty() || type.isEmpty() || amount.isEmpty()) {
                    // Tampilkan pesan error jika ada input yang belum diisi
                    if (binding.tvCategory.getText().toString().equalsIgnoreCase("Category")) {
                        category = "18";
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
                    tvMessage.setText("Saving data");

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
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout llFoodAndDrink = dialog.findViewById(R.id.ll_foodAndDrink);
        LinearLayout llShopping = dialog.findViewById(R.id.ll_shopping);
        LinearLayout llTransport = dialog.findViewById(R.id.ll_transport);
        LinearLayout llHome = dialog.findViewById(R.id.ll_home);
        LinearLayout llBillsAndFees = dialog.findViewById(R.id.ll_billsAndFees);
        LinearLayout llEntertainment = dialog.findViewById(R.id.ll_entertainment);
        LinearLayout llVehicle = dialog.findViewById(R.id.ll_vehicle);
        LinearLayout llTravel = dialog.findViewById(R.id.ll_travel);
        LinearLayout llFamilyAndPersonal = dialog.findViewById(R.id.ll_familyAndPersonal);
        LinearLayout llHealthcare = dialog.findViewById(R.id.ll_healthcare);
        LinearLayout llEducation = dialog.findViewById(R.id.ll_education);
        LinearLayout llGroceries = dialog.findViewById(R.id.ll_groceries);
        LinearLayout llGifts = dialog.findViewById(R.id.ll_gifts);
        LinearLayout llSportsAndHobby = dialog.findViewById(R.id.ll_sportsAndHobby);
        LinearLayout llBeauty = dialog.findViewById(R.id.ll_beauty);
        LinearLayout llWork = dialog.findViewById(R.id.ll_work);
        LinearLayout llPet = dialog.findViewById(R.id.ll_pet);
        LinearLayout llOther = dialog.findViewById(R.id.ll_other);

        llFoodAndDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "1";
                binding.tvCategory.setText("Food and Drink");
                dialog.dismiss();
            }
        });

        llShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "2";
                binding.tvCategory.setText("Shopping");
                dialog.dismiss();
            }
        });

        llTransport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "3";
                binding.tvCategory.setText("Transport");
                dialog.dismiss();
            }
        });

        llHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "4";
                binding.tvCategory.setText("Home");
                dialog.dismiss();
            }
        });

        llBillsAndFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "5";
                binding.tvCategory.setText("Bills and Fees");
                dialog.dismiss();
            }
        });

        llEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "6";
                binding.tvCategory.setText("6");
                dialog.dismiss();
            }
        });

        llVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "7";
                binding.tvCategory.setText("Vehicle");
                dialog.dismiss();
            }
        });

        llTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "8";
                binding.tvCategory.setText("Travel");
                dialog.dismiss();
            }
        });

        llFamilyAndPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "9";
                binding.tvCategory.setText("Family and Personal");
                dialog.dismiss();
            }
        });

        llHealthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "10";
                binding.tvCategory.setText("Healthcare");
                dialog.dismiss();
            }
        });

        llEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "11";
                binding.tvCategory.setText("Education");
                dialog.dismiss();
            }
        });

        llGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "12";
                binding.tvCategory.setText("Groceries");
                dialog.dismiss();
            }
        });

        llGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "13";
                binding.tvCategory.setText("Gifts");
                dialog.dismiss();
            }
        });

        llSportsAndHobby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "14";
                binding.tvCategory.setText("Sport and Hobby");
                dialog.dismiss();
            }
        });

        llBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "15";
                binding.tvCategory.setText("Beauty");
                dialog.dismiss();
            }
        });

        llWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "16";
                binding.tvCategory.setText("Work");
                dialog.dismiss();
            }
        });

        llPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "17";
                binding.tvCategory.setText("Pet");
                dialog.dismiss();
            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "18";
                binding.tvCategory.setText("Other");
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
                        binding.tvCategory.getText();
                        binding.etNote.getEditText().setText("");
                        binding.etAmount.getEditText().setText("");
                        Toast.makeText(getActivity(), pesan, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), pesan + " " + category, Toast.LENGTH_SHORT).show();
                    }
                    dialog.dismiss();
                } else {
                    Toast.makeText(getActivity(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
