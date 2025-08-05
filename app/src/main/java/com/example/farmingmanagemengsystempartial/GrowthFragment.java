package com.example.farmingmanagemengsystempartial;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;

public class GrowthFragment extends Fragment {
    Dialog dialog;
    ImageView back;
    Button addGrowthData;
    private WeightBarView weightBarView;

    private Date initialUpdateDate; // Track the initial update date
    private static final String PREFS_NAME = "GrowthPrefs";
    private static final String KEY_INITIAL_DATE = "initialUpdateDate";
    private static final String KEY_TRACKING_START_DATE = "trackingStartDate"; // New preference key

    // Track selected date
    private Date selectedDate = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_growth, container, false);

        // Load the initial update date from SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long initialDateMillis = prefs.getLong(KEY_INITIAL_DATE, -1);
        if (initialDateMillis != -1) {
            initialUpdateDate = new Date(initialDateMillis);
        }

        // For setting text in the panel
        TextView sizeValString = view.findViewById(R.id.sizeValue);
        TextView dayValString = view.findViewById(R.id.daysOldValue);
        TextView averageWeightValString = view.findViewById(R.id.averageWeightValue);
        TextView targetValString = view.findViewById(R.id.targetValue);

        weightBarView = view.findViewById(R.id.weightBar);
        weightBarView.setAverageWeight(2.5f); // Example weight

        addGrowthData = view.findViewById(R.id.Add_growth_Data_Btn);
        addGrowthData.setOnClickListener(v -> showAddGrowthDataDialog(sizeValString, dayValString, averageWeightValString, targetValString));

        back = view.findViewById(R.id.ReturnBtn);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void showAddGrowthDataDialog(TextView sizeValString, TextView dayValString, TextView averageWeightValString, TextView targetValString) {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_growth);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT); // Set dialog width

        // Fetch values from dialog input
        EditText sizeEdit = dialog.findViewById(R.id.editTextNumber);
        EditText sampleSizeEdit = dialog.findViewById(R.id.editTextNumber2);
        LinearLayout sampleSizeLayout = dialog.findViewById(R.id.sampleSizeLayout);
        Button generateSamplesButton = dialog.findViewById(R.id.generateSamplesButton);
        Button applyChanges = dialog.findViewById(R.id.ApplyChangesBTN);
        Button datePickerButton = dialog.findViewById(R.id.datePickerBtn);

        // Initialize the date picker
        //initDatePicker(datePickerButton);

        // Sample size restriction
        sampleSizeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    sampleSizeEdit.setError("Sample size cannot be empty");
                } else {
                    sampleSizeEdit.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Generate dynamic EditTexts for sample weights
        generateSamplesButton.setOnClickListener(v -> {
            sampleSizeLayout.removeAllViews(); // Clear previous inputs
            int sampleSize;
            try {
                sampleSize = Integer.parseInt(sampleSizeEdit.getText().toString());
                for (int i = 0; i < sampleSize; i++) {
                    EditText sampleEditText = new EditText(getActivity());
                    sampleEditText.setHint("Weight of chicken " + (i + 1) + " in kg");
                    sampleEditText.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    sampleSizeLayout.addView(sampleEditText);
                }
            } catch (NumberFormatException e) {
                sampleSizeEdit.setError("Enter a valid number");
            }
        });

        // Initialize the date picker
        datePickerButton.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select start date")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build();
            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate = new Date(selection);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(selection);
                datePickerButton.setText(makeDateString(
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR)
                ));

                //if selected date is in the future
                if (selectedDate.after(new Date())) {
                    Toast.makeText(getActivity(), "Selected date cannot be in the future.", Toast.LENGTH_SHORT).show();
                    selectedDate = null; // Reset selected date
                    return;
                }

                // Save the selected date as tracking start date immediately
                SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(KEY_TRACKING_START_DATE, selectedDate.getTime());
                editor.apply();
            });
        });

        // Dialog box
        applyChanges.setOnClickListener(v -> {
            String sizeString = sizeEdit.getText().toString();
            String sampleSizeString = sampleSizeEdit.getText().toString();

            if (!sizeString.isEmpty() && !sampleSizeString.isEmpty()) {
                int sampleSize = Integer.parseInt(sampleSizeString);
                float totalWeight = 0;
                boolean allWeightsFilled = true;

                for (int i = 0; i < sampleSize; i++) {
                    EditText sampleWeightEdit = (EditText) sampleSizeLayout.getChildAt(i);
                    if (sampleWeightEdit != null && !sampleWeightEdit.getText().toString().isEmpty()) {
                        totalWeight += Float.parseFloat(sampleWeightEdit.getText().toString());
                    } else {
                        allWeightsFilled = false;
                        break;
                    }
                }

                if (!allWeightsFilled) {
                    Toast.makeText(getActivity(), "Please fill all weight fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedDate == null) {
                    Toast.makeText(getActivity(), "Please select a valid start date", Toast.LENGTH_SHORT).show();
                    return;
                }

                float averageWeight = totalWeight / sampleSize;
                float deviation = ((averageWeight - 3.0f) / 3.0f) * 100; // Calculating deviation in percentage

                // Set initial date if it's the first update
                if (initialUpdateDate == null) {
                    initialUpdateDate = new Date(); // Set the initial update date
                    SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putLong(KEY_INITIAL_DATE, initialUpdateDate.getTime());
                    editor.apply();
                }

                // Always calculate days from the tracked start date
                SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                long startDateMillis = prefs.getLong(KEY_TRACKING_START_DATE, System.currentTimeMillis());
                Date startDate = new Date(startDateMillis);
                int daysOld = calculateDaysOld(startDate);

                sizeValString.setText(sizeString);
                dayValString.setText(String.valueOf(daysOld)); // Update days old
                averageWeightValString.setText(String.format("%.2f/3.0kg", averageWeight));
                targetValString.setText(String.format("%.2f%%", deviation));

            } else {
                Toast.makeText(getActivity(), "Please fill required fields.", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private int calculateDaysOld(Date startDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar currentCal = Calendar.getInstance();
        long diffInMillis = currentCal.getTimeInMillis() - startCal.getTimeInMillis();
        return (int) (diffInMillis / (1000 * 60 * 60 * 24)) + 1; // +1 to count the first day
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1: return "JAN";
            case 2: return "FEB";
            case 3: return "MAR";
            case 4: return "APR";
            case 5: return "MAY";
            case 6: return "JUN";
            case 7: return "JUL";
            case 8: return "AUG";
            case 9: return "SEP";
            case 10: return "OCT";
            case 11: return "NOV";
            case 12: return "DEC";
            default: return "JAN"; // Default should never happen
        }
    }
}