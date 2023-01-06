package com.jaya.financia.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.jaya.financia.API.APIRequestData;
import com.jaya.financia.API.RetroServer;
import com.jaya.financia.Adapter.ViewPagerAdapter;
import com.jaya.financia.ExpensesFragment;
import com.jaya.financia.IncomesFragment;
import com.jaya.financia.Model.ResponseModel;
import com.jaya.financia.R;
import com.jaya.financia.databinding.ActivityAddBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends AppCompatActivity {
    ActivityAddBinding binding;
    private String user_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");
        String someValue = bundle.getString("user_uid");

        // Menyiapkan bundle untuk dikirimkan ke fragment
        Bundle bundle1 = new Bundle();
        bundle1.putString("user_uid", someValue);
        Bundle bundle2 = new Bundle();
        bundle2.putString("user_uid", someValue);

        // Menyiapkan fragment
        ExpensesFragment expensesFragment = new ExpensesFragment();
        expensesFragment.setArguments(bundle1);
        IncomesFragment incomesFragment = new IncomesFragment();
        incomesFragment.setArguments(bundle2);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFragment(expensesFragment,"Expenses");
        viewPagerAdapter.addFragment(incomesFragment,"Incomes");
        binding.viewPager.setAdapter(viewPagerAdapter);

        binding.tabLayoutNotes.setupWithViewPager(binding.viewPager);
        binding.tabLayoutNotes.getTabAt(0).setIcon(R.drawable.ic_expenses);
        binding.tabLayoutNotes.getTabAt(1).setIcon(R.drawable.ic_incomes);

        getSupportActionBar().setTitle("New Transaction");
//
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}