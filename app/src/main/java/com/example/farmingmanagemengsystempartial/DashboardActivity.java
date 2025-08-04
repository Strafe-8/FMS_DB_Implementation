package com.example.farmingmanagemengsystempartial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements
        GrowthFragment.OnGrowthDataChangedListener,
        FeedsFragment.OnFeedDataChangedListener,
        MortalityFragment.OnMortalityDataChangedListener {

    private HorizontalBarChart barChart;
    private float healthStatus = 0;
    private float totalFeeds = 0;
    private int totalChickens = 0;  // Initialized to 0
    private int totalDeaths = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        barChart = findViewById(R.id.barChart);
        setupBarChart();

        ImageView animalTrack = findViewById(R.id.trackingTab);
        ImageView product = findViewById(R.id.productTab);
        ImageView educationalResources = findViewById(R.id.eduResourceTab);

        animalTrack.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AnimalTrackActivity.class);
            startActivity(intent);
        });

        product.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ProductActivity.class);
            startActivity(intent);
        });

        educationalResources.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, EducationalResourcesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onGrowthDataChanged(float healthStatus) {
        this.healthStatus = healthStatus;
        updateBarChart();
    }

    @Override
    public void onFeedDataChanged(float totalFeeds) {
        this.totalFeeds = totalFeeds;
        updateBarChart();
    }

    @Override
    public void onMortalityDataChanged(int totalChickens, int totalDeaths) {
        this.totalChickens = totalChickens;
        this.totalDeaths = totalDeaths;
        updateBarChart();
    }

    public void updateMortalityFragment(int totalChickens) {
        MortalityFragment mortalityFragment = (MortalityFragment) getSupportFragmentManager()
                .findFragmentByTag("MORTALITY_FRAGMENT"); // Use the tag you set when adding the fragment
        if (mortalityFragment != null) {
            mortalityFragment.updateChickens(totalChickens);
        }
    }

    private void updateBarChart() {
        setupBarChart();
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, totalDeaths)); // Mortality Rate
        entries.add(new BarEntry(1, totalFeeds)); // Total Feeds
        entries.add(new BarEntry(2, healthStatus)); // Health Status

        BarDataSet dataSet = new BarDataSet(entries, "Farm Metrics");
        int[] colors = {
                getResources().getColor(R.color.mortality_rate),
                getResources().getColor(R.color.growth_progress),
                getResources().getColor(R.color.health_status)
        };
        dataSet.setColors(colors);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
    }
}