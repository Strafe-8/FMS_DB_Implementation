package com.example.farmingmanagemengsystempartial;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MortalityFragment extends Fragment {

    Dialog dialog;
    ImageView AddReportData, applyChanges;
    TextView remainingChickensText, mortalityRateText;
    Calendar calendar;
    RecyclerView recyclerView;
    MortalityAdapter adapter;
    List<MortalityData> dataList = new ArrayList<>();

    int total_chickens = 100;
    int mortality_rate = 0;
    int totalDeaths = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mortality, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        remainingChickensText = view.findViewById(R.id.some_id6);
        mortalityRateText = view.findViewById(R.id.some_id7);
        remainingChickensText.setText(String.valueOf(total_chickens));

        recyclerView = view.findViewById(R.id.MortalityDataList);
        adapter = new MortalityAdapter(dataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        AddReportData = view.findViewById(R.id.add_feeding_box2);
        AddReportData.setOnClickListener(v -> {
            dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.add_report_data);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            calendar = Calendar.getInstance();
            EditText dialogDate = dialog.findViewById(R.id.dateEditText);
            EditText dialogDeaths = dialog.findViewById(R.id.DeathCountEditText);
            EditText dialogSick = dialog.findViewById(R.id.SickCountEditText);
            EditText dialogNotes = dialog.findViewById(R.id.NotesEditText);

            dialogDate.setOnClickListener(v2 -> showDatePicker(dialogDate));

            applyChanges = dialog.findViewById(R.id.imageView38);
            applyChanges.setOnClickListener(v2 -> {
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
            });

            dialog.show();
        });

    }

    private void showDatePicker(EditText dialogDate) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dialogDate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}