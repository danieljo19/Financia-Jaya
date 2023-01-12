package com.jaya.financia.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Model.AnalyticExpensesModel;
import com.jaya.financia.Model.AnalyticIncomesModel;
import com.jaya.financia.Model.ResponseAnalyticExpenses;
import com.jaya.financia.Model.ResponseAnalyticIncomes;
import com.jaya.financia.R;
import com.jaya.financia.databinding.ActivityAnalyticBinding;

import java.util.ArrayList;
import java.util.List;

import maes.tech.intentanim.CustomIntent;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticActivity extends AppCompatActivity {
    ActivityAnalyticBinding binding;

    BarChart barChart;
    PieChart pieChart;

    private FirebaseAuth mAuth;
    private List<AnalyticIncomesModel> listDataIncomes = new ArrayList<>();
    private List<AnalyticExpensesModel> listDataExpenses = new ArrayList<>();
    private String user_uid, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        user_uid = mAuth.getUid();

        binding = ActivityAnalyticBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Analytic");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        user_uid = bundle.getString("user_uid");

        binding.bottomNavigation.setSelectedItemId(R.id.item_2);
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_1:
                        Intent intentMain = new Intent(AnalyticActivity.this, MainActivity.class);
                        Bundle bundleMain = new Bundle();
                        bundleMain.putString("user_uid", user_uid);
                        intentMain.putExtra("data", bundleMain);
                        startActivity(intentMain);
                        CustomIntent.customType(AnalyticActivity.this, "right-to-left");
                        return true;
                    case R.id.item_2:
                        return true;
                    case R.id.item_3:
                        Intent intentSetting = new Intent(AnalyticActivity.this, SettingActivity.class);
                        Bundle bundleSetting = new Bundle();
                        bundleSetting.putString("user_uid", user_uid);
                        intentSetting.putExtra("data", bundleSetting);
                        startActivity(intentSetting);
                        CustomIntent.customType(AnalyticActivity.this, "left-to-right");
                        return true;
                }
                return false;
            }
        });

        barChart = binding.idBarChart;
        pieChart = binding.idPieChart;

        retrieveBarChartData();
        retrievePieChartData();
    }

    private void retrievePieChartData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseAnalyticExpenses> tampilData = api.ardGetAnalyticExpensesMonthly(user_uid);

        tampilData.enqueue(new Callback<ResponseAnalyticExpenses>() {
            @Override
            public void onResponse(Call<ResponseAnalyticExpenses> call, Response<ResponseAnalyticExpenses> response) {
                int kode = response.body().getKode();
                listDataExpenses = response.body().getData();
                ArrayList<PieEntry> pieEntries = new ArrayList<>();

                if (kode == 1) {
                    for (int i = 0; i < listDataExpenses.size(); i++) {
                        AnalyticExpensesModel data = listDataExpenses.get(i);
                        float amount = Float.parseFloat(data.getTotal());
                        String category = data.getCategory();
                        pieEntries.add(new PieEntry(amount, category));
                    }
                    PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
                    pieDataSet.setValueTextSize(12f);
                    pieDataSet.setValueFormatter(new PercentFormatter(pieChart));
                    pieDataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieChart.setUsePercentValues(true);
                    pieChart.setEntryLabelTextSize(12f);
                    pieChart.setEntryLabelTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setCenterText("Expenses");
                    pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    pieChart.getLegend().setTextColor(getResources().getColor(R.color.neutral_80));
                    pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieChart.notifyDataSetChanged();
                    pieChart.invalidate();
                    binding.progressBarExpenses.setVisibility(View.GONE);
                } else {
                    Toast.makeText(AnalyticActivity.this, "Can't retrieve data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnalyticExpenses> call, Throwable t) {

            }
        });
    }

    public void retrieveBarChartData() {
        APIRequestData api = RetroServer.konekRetrofit().create(APIRequestData.class);
        Call<ResponseAnalyticIncomes> tampilData = api.ardGetAnalyticIncomesYearly(user_uid);

        tampilData.enqueue(new Callback<ResponseAnalyticIncomes>() {
            @Override
            public void onResponse(Call<ResponseAnalyticIncomes> call, Response<ResponseAnalyticIncomes> response) {
                int kode = response.body().getKode();
                listDataIncomes = response.body().getData();
                ArrayList<BarDataSet> barDataSets = new ArrayList<>();
                BarData barData = new BarData();

                if (kode == 1) {
                    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                    for (int i = 0; i < listDataIncomes.size(); i++) {
                        AnalyticIncomesModel data = listDataIncomes.get(i);
                        Float amount = Float.parseFloat(data.getTotal_incomes());
                        String month = monthNames[Integer.parseInt(data.getMonth()) - 1];
                        ArrayList<BarEntry> barEntries = new ArrayList<>();
                        barEntries.add(new BarEntry(i, amount));
                        BarDataSet barDataSet = new BarDataSet(barEntries, month);
                        barDataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
                        barDataSet.setValueTextSize(12f);
                        barDataSet.setValueTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        barData.addDataSet(barDataSet);
                    }
                    barChart.setData(barData);
                    barChart.setPinchZoom(false);
                    barChart.setDragEnabled(false);
                    barChart.setScaleEnabled(false);
                    barChart.setDrawGridBackground(false);
                    barChart.setDoubleTapToZoomEnabled(false);
                    barChart.getAxisLeft().setDrawLabels(false);
                    barChart.getAxisRight().setTextColor(getResources().getColor(R.color.neutral_80));
                    barChart.getXAxis().setDrawLabels(false);
                    barChart.getDescription().setEnabled(false);
                    barChart.notifyDataSetChanged();
                    barChart.invalidate();
                    Legend legend = barChart.getLegend();
                    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    legend.setTextColor(getResources().getColor(R.color.neutral_80));
                    binding.progressBarIncomes.setVisibility(View.GONE);
                } else {
                    Toast.makeText(AnalyticActivity.this, "Can't retrieve data. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnalyticIncomes> call, Throwable t) {
                Toast.makeText(AnalyticActivity.this, "Failed to connect to the server.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}