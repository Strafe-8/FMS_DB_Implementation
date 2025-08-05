package com.example.farmingmanagemengsystempartial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AnimalTrackActivity extends AppCompatActivity {
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

        // Set the TabLayout background color
        tabLayout.setBackgroundColor(getResources().getColor(R.color.neon_green));

        TabAdapter adapter = new TabAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
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
            Intent intent = new Intent(AnimalTrackActivity.this, DashboardActivity.class);
            startActivity(intent);
        });
    }
}