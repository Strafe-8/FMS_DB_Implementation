package com.example.farmingmanagemengsystempartial;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnimalTrackMortality extends AppCompatActivity {
    Dialog dialog;
    ImageView Back, AddReportData, applyChanges;
    TextView Feeds, Growth, remainingChickensText, mortalityRateText;
    Calendar calendar;
    RecyclerView recyclerView;
    MortalityAdapter adapter;
    List<MortalityData> dataList = new ArrayList<>();

    int total_chickens = 100;
    int mortality_rate = 0;
    int totalDeaths = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_animal_track_mortality);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        remainingChickensText = findViewById(R.id.some_id6);
        mortalityRateText = findViewById(R.id.some_id7);
        remainingChickensText.setText(String.valueOf(total_chickens));
        recyclerView = findViewById(R.id.MortalityDataList);
        adapter = new MortalityAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        AddReportData = findViewById(R.id.add_feeding_box2);
        AddReportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AnimalTrackMortality.this);
                dialog.setContentView(R.layout.add_report_data);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                calendar = Calendar.getInstance();
                EditText dialogDate = dialog.findViewById(R.id.dateEditText);
                dialogDate.setOnClickListener(v2 -> showDatePicker(dialogDate));
                EditText dialogDeaths = dialog.findViewById(R.id.DeathCountEditText);
                EditText dialogSick = dialog.findViewById(R.id.SickCountEditText);
                EditText dialogNotes = dialog.findViewById(R.id.NotesEditText);



                applyChanges = dialog.findViewById(R.id.imageView38);
                applyChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String date = dialogDate.getText().toString();
                        String deaths = dialogDeaths.getText().toString();
                        String sick = dialogSick.getText().toString();
                        String notes = dialogNotes.getText().toString();

                        int deathCount = 0;
                        try {
                            deathCount = Integer.parseInt(deaths);
                        } catch (NumberFormatException e) {
                            deathCount = 0;
                        }

                        totalDeaths += deathCount;
                        total_chickens -= deathCount;
                        if (total_chickens < 0) total_chickens = 0;

                        remainingChickensText.setText(String.valueOf(total_chickens));

                        int mortalityRate = (int) ((totalDeaths / 100.0) * 100);
                        String mortalityRateString = mortalityRate + "%";
                        mortalityRateText.setText(mortalityRateString);

                        dataList.add(new MortalityData(date, deaths, sick, notes));
                        adapter.notifyItemInserted(dataList.size() - 1);
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }

            private void showDatePicker(EditText dialogDate) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AnimalTrackMortality.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            dialogDate.setText(selectedDate);                        },
                        year, month, day
                );

                datePickerDialog.show();
            }

        });




        Back = findViewById(R.id.imageView31);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackMortality.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        Feeds = findViewById(R.id.feeds3);
        Feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackMortality.this, AnimalTrackFeeds.class);
                startActivity(intent);
            }
        });

        Growth = findViewById(R.id.growth3);
        Growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AnimalTrackMortality.this, AnimalTrackActivity.class);
                startActivity(intent);
            }
        });


    }


}