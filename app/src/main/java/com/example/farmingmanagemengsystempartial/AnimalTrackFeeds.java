package com.example.farmingmanagemengsystempartial;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AnimalTrackFeeds extends AppCompatActivity {
    Dialog dialog;
    ImageView Back, AddFeedData, applyChanges;
    TextView Growth, Mortality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_track_feeds);


        AddFeedData = findViewById(R.id.add_feeding_box);
        AddFeedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AnimalTrackFeeds.this);
                dialog.setContentView(R.layout.add_feed_data);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


                applyChanges = dialog.findViewById(R.id.imageView35);

                applyChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Growth = findViewById(R.id.growth2);
        Growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackFeeds.this, AnimalTrackActivity.class);
                startActivity(intent);
            }
        });

        Mortality = findViewById(R.id.mortality2);
        Mortality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackFeeds.this, AnimalTrackMortality.class);
                startActivity(intent);
            }
        });

        Back = findViewById(R.id.imageView2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackFeeds.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}