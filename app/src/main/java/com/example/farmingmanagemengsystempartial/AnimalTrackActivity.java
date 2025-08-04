package com.example.farmingmanagemengsystempartial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnimalTrackActivity extends AppCompatActivity
        implements GrowthFragment.OnGrowthDataChangedListener,
        FeedsFragment.OnFeedDataChangedListener,
        MortalityFragment.OnMortalityDataChangedListener {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_track);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        back = findViewById(R.id.ReturnBtn);

        // Set TabLayout background color (works on your side)
        tabLayout.setBackgroundColor(getResources().getColor(R.color.neon_green));

        // Set tab text color: white when selected and unselected
        tabLayout.setTabTextColors(
                getResources().getColor(android.R.color.white),   // unselected
                getResources().getColor(android.R.color.white)    // selected
        );

        // Set tab indicator (hover bar) color
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white));

        TabAdapter adapter = new TabAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Growth");
                    break;
                case 1:
                    tab.setText("Feeds");
                    break;
                case 2:
                    tab.setText("Mortality");
                    break;
            }
        }).attach();

        back.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    // Required interface implementations
    @Override
    public void onGrowthDataChanged(float healthStatus) {
        // Leave empty for now
    }

    @Override
    public void onFeedDataChanged(float totalFeeds) {
        // Leave empty for now
    }

    @Override
    public void onMortalityDataChanged(int totalChickens, int totalDeaths) {
        // Leave empty for now
    }
}
