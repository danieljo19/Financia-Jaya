package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
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
    private String category;
    private String tvCategory, tvCategoryIncomes;

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

        if(xCat.equalsIgnoreCase("1")) {
            binding.tvCategory.setText("Food and Drink");
            category = "1";
            tvCategory = "Food and Drink";
        } else if(xCat.equalsIgnoreCase("2")) {
            binding.tvCategory.setText("Shopping");
            category = "2";
            tvCategory = "Shopping";
        } else if(xCat.equalsIgnoreCase("3")) {
            binding.tvCategory.setText("Transport");
            category = "3";
            tvCategory = "Transport";
        } else if(xCat.equalsIgnoreCase("4")) {
            binding.tvCategory.setText("Home");
            category = "4";
            tvCategory = "Home";
        } else if(xCat.equalsIgnoreCase("5")) {
            binding.tvCategory.setText("Bills and Fees");
            category = "5";
            tvCategory = "Bills and Fees";
        } else if(xCat.equalsIgnoreCase("6")) {
            binding.tvCategory.setText("Entertainment");
            category = "6";
            tvCategory = "Entertainment";
        } else if(xCat.equalsIgnoreCase("7")) {
            binding.tvCategory.setText("Vehicle");
            category = "7";
            tvCategory = "Vehicle";
        } else if(xCat.equalsIgnoreCase("8")) {
            binding.tvCategory.setText("Travel");
            category = "8";
            tvCategory = "Travel";
        } else if(xCat.equalsIgnoreCase("9")) {
            binding.tvCategory.setText("Family and Personal");
            category = "9";
            tvCategory = "Family and Personal";
        } else if(xCat.equalsIgnoreCase("10")) {
            binding.tvCategory.setText("Healthcare");
            category = "10";
            tvCategory = "Healthcare";
        } else if(xCat.equalsIgnoreCase("11")) {
            binding.tvCategory.setText("Education");
            category = "11";
            tvCategory = "Education";
        } else if(xCat.equalsIgnoreCase("12")) {
            binding.tvCategory.setText("Grocery");
            category = "12";
            tvCategory = "Grocery";
        } else if(xCat.equalsIgnoreCase("13")) {
            binding.tvCategory.setText("Gifts");
            category = "13";
            tvCategory = "Gifts";
        } else if(xCat.equalsIgnoreCase("14")) {
            binding.tvCategory.setText("Sports and Hobby");
            category = "14";
            tvCategory = "Sports and Hobby";
        } else if(xCat.equalsIgnoreCase("15")) {
            binding.tvCategory.setText("Beauty");
            category = "15";
            tvCategory = "Beauty";
        } else if(xCat.equalsIgnoreCase("16")) {
            binding.tvCategory.setText("Work");
            category = "16";
            tvCategory = "Work";
        } else if(xCat.equalsIgnoreCase("17")) {
            binding.tvCategory.setText("Pet");
            category = "17";
            tvCategory = "Pet";
        } else {
            binding.tvCategory.setText("Other");
            category = "18";
            tvCategory = "Other";
        }

        if(xCat.equalsIgnoreCase("1")) {
            binding.tvCategory.setText("Salary");
            category = "1";
            tvCategoryIncomes = "Salary";
        } else if(xCat.equalsIgnoreCase("2")) {
            binding.tvCategory.setText("Business");
            category = "2";
            tvCategoryIncomes = "Business";
        } else if(xCat.equalsIgnoreCase("3")) {
            binding.tvCategory.setText("Gifts");
            category = "3";
            tvCategoryIncomes = "Gifts";
        } else if(xCat.equalsIgnoreCase("4")) {
            binding.tvCategory.setText("Extra Incomes");
            category = "4";
            tvCategoryIncomes = "Extra Incomes";
        } else if(xCat.equalsIgnoreCase("5")) {
            binding.tvCategory.setText("Loan");
            category = "5";
            tvCategoryIncomes = "Loan";
        } else if(xCat.equalsIgnoreCase("6")) {
            binding.tvCategory.setText("Investment");
            category = "6";
            tvCategoryIncomes = "Investment";
        } else if(xCat.equalsIgnoreCase("7")) {
            binding.tvCategory.setText("Insurance Payout");
            category = "7";
            tvCategoryIncomes = "Insurance Payout";
        } else {
            binding.tvCategory.setText("Other");
            category = "8";
            tvCategoryIncomes = "Other";
        }

        xCat = category;

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
            binding.tvCategory.setText(tvCategory);
        } else {
            binding.tvCategory.setText(tvCategoryIncomes);
            binding.tvEdit.setText("Edit incomes");
        }

        binding.btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(xType.equalsIgnoreCase("expenses")) {
                    showDialogExpenses();
                } else {
                    showDialogIncomes();
                }
            }
        });

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
                yCat = category;
                category = binding.tvCategory.getText().toString();
                yAmount = binding.etAmount.getEditText().getText().toString();

                updateData();
            }
        });
    }

    private void showDialogIncomes() {
        final Dialog dialog = new Dialog(EditActivity.this);
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
                category = "1";
                binding.tvCategory.setText("Salary");
                dialog.dismiss();
            }
        });

        llBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "2";
                binding.tvCategory.setText("Business");
                dialog.dismiss();
            }
        });

        llGifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "3";
                binding.tvCategory.setText("Gifts");
                dialog.dismiss();
            }
        });

        llExtraIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "4";
                binding.tvCategory.setText("Extra Income");
                dialog.dismiss();
            }
        });

        llLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "5";
                binding.tvCategory.setText("Loan");
                dialog.dismiss();
            }
        });

        llInvestment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "6";
                binding.tvCategory.setText("Investment");
                dialog.dismiss();
            }
        });

        llInsurancePayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "7";
                binding.tvCategory.setText("Insurance Payout");
                dialog.dismiss();
            }
        });

        llOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "8";
                binding.tvCategory.setText("Other");
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void showDialogExpenses() {
        final Dialog dialog = new Dialog(EditActivity.this);
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
                binding.tvCategory.setText("Entertainment");
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