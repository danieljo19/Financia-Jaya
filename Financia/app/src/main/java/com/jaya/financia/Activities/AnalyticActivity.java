package com.jaya.financia.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.DataModel;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.Model.UserModel;
import com.jaya.financia.R;
import com.jaya.financia.User;
import com.jaya.financia.databinding.ActivityAnalyticBinding;

import java.time.format.DateTimeFormatter;
import java.time.format.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticActivity extends AppCompatActivity {
    ActivityAnalyticBinding binding;

    // variable for our bar chart
    BarChart barChart;
    PieChart pieChart;

    // variable for our bar data.
    BarData barData;
    PieData pieData;

    // variable for our bar data set.
    BarDataSet barDataSet;
    PieDataSet pieDataSet;

    // array list for storing entries.
    ArrayList barEntriesArrayList;
    ArrayList pieEntriesArrayList;
    private FirebaseAuth mAuth;

    private List<DataModel> listData = new ArrayList<>();
    private String user_uid, type, test, date, day, dateList, EMonth, EDay, month ="";
    private int id, size;
    private int Tot;
    private int InMoney, OutMoney, tot, totOut = 0;
    private UserModel userModel = new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        User user = new User();
        user_uid = mAuth.getUid();

        binding = ActivityAnalyticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user_uid = bundle.getString("user_uid");

        binding.bottomNavigation.setSelectedItemId(R.id.item_2);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.item_1:
                        Intent intentMain = new Intent(AnalyticActivity.this, MainActivity.class);
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("user_uid", user_uid);
                        intentMain.putExtra("data", bundleMain);
                        startActivity(intentMain);
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.item_2:
                        return true;
                    case R.id.item_3:
                        Intent intentSetting = new Intent(AnalyticActivity.this, SettingActivity.class);
                        Bundle bundleSetting = new Bundle();
                        bundleSetting.putString("user_uid", user_uid);
                        intentSetting.putExtra("data", bundleSetting);
                        startActivity(intentSetting);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        retrieveData();
//        getBarEntries();
        getPieEntries();
        binding.test.setText(String.valueOf(Tot));

        barChart = binding.idBarChart;
        pieChart = binding.idPieChart;

        barDataSet = new BarDataSet(barEntriesArrayList, "");
        pieDataSet = new PieDataSet(pieEntriesArrayList, "");

        barData = new BarData(barDataSet);
        pieData = new PieData(pieDataSet);

        barChart.setData(barData);
        pieChart.setData(pieData);

        barDataSet.setColors(getResources().getColor(android.R.color.holo_green_dark));
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        // setting text color.
        barDataSet.setValueTextColor(getResources().getColor(android.R.color.holo_green_dark));
        pieDataSet.setValueTextColor(getResources().getColor(android.R.color.holo_red_light));

        // setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(false);

        pieDataSet.setValueTextSize(16f);
        pieChart.getDescription().setEnabled(false);

//        binding.test.setText(test);

        getSupportActionBar().setTitle("Analytic");
    }

    private void getBarEntries() {
        // creating a new array list
        barEntriesArrayList = new ArrayList<>();
//        binding.test.setText(String.valueOf(Tot));


//         adding new entry to our array list with bar
//         entry and passing x and y axis value to it.
//        for (int i = 0; i < size; i++) {
            barEntriesArrayList.add(new BarEntry(1f, 1));
//        }
    }

    private void getPieEntries() {
        // creating a new array list
        pieEntriesArrayList = new ArrayList<>();

//         adding new entry to our array list with bar
//         entry and passing x and y axis value to it.
        pieEntriesArrayList.add(new PieEntry(1f, 4));
        pieEntriesArrayList.add(new PieEntry(2f, 6));
        pieEntriesArrayList.add(new PieEntry(3f, 8));
        pieEntriesArrayList.add(new PieEntry(4f, 2));
        pieEntriesArrayList.add(new PieEntry(5f, 4));
        pieEntriesArrayList.add(new PieEntry(6f, 1));
    }

    public void retrieveData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseModel> tampilData = api.ardRetrieveData(user_uid);

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();
                listData = response.body().getData();
                size = listData.size();
//                String test = listData.get(0).getDate();
//                date = listData.get(0).getDate();
//                String[] parts = date.split("-");
//                String month = parts[1];

                if (kode == 1) {
                    // Data ditemukan
                    for (int i = 0; i < size; i++){
                        date = listData.get(i).getDate();
                        String[] numbah = date.split("-");
                        day = numbah[0];
                        month = numbah[1];
                        for (int a = 0; a < size; a++){
                           dateList = listData.get(a).getDate();
                           String[] numbah2 = dateList.split("-");
                           EMonth = numbah2[1];
                           type = listData.get(a).getType();
//                           if (month == "01") {
                               if (type == "income"){
                                   InMoney = Integer.parseInt(listData.get(0).getAmount());
                                   tot = InMoney + tot;
                                   Tot = tot;
                               }else {
                                   OutMoney = Integer.parseInt(listData.get(0).getAmount());
                                   totOut = totOut - OutMoney;
                                   Tot = totOut;
                               }
//                           } else {
//                               break;
//                           }
                        }
                        break;
                    }
                    barEntriesArrayList = new ArrayList<>();
                    barEntriesArrayList.add(new BarEntry(1f, Tot));

//                    for (DataModel data : listData) {
//                        String type = data.getType();
//                        String date = data.getDate();
//                        String amount = data.getAmount();
//                        if (type == "Outcome"){
//                            binding.test.setText("Works");
//                        }else{
//                            binding.test.setText("failed");
//                        }
//                    }

                } else {
                    // Data tidak ditemukan
                }
            }
            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(AnalyticActivity.this, "Gagal terhubung ke server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}