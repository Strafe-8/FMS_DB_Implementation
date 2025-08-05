package com.example.farmingmanagemengsystempartial;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FeedsFragment extends Fragment {
    private Dialog dialog;
    private Button addFeedingDataBtn;
    private TextView currentFeedTypeTextView;
    private TextView todaysValueTextView;
    private TextView totalFeedsValueTextView; // TextView to display total feeds
    private BarChart feedChart;

    private ArrayList<BarEntry> entries; // To hold feeding data
    private BarDataSet dataSet; // To hold the dataset for the chart
    private ArrayList<String> xLabels; // To hold X-axis labels (dates)

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());
    private Date selectedDate; // To store the selected date
    private float totalFeeds = 0; // Variable to keep a running total of feeds

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        addFeedingDataBtn = view.findViewById(R.id.Add_Feeding_Data_Btn);
        currentFeedTypeTextView = view.findViewById(R.id.CurrentFeedTypes);
        todaysValueTextView = view.findViewById(R.id.TodaysValue);
        totalFeedsValueTextView = view.findViewById(R.id.totalFeedsValue); // Initialize total feeds TextView
        feedChart = view.findViewById(R.id.feed_chart); // Initialize BarChart

        entries = new ArrayList<>(); // Initialize the entries list
        xLabels = new ArrayList<>(); // Initialize the X-axis labels list

        // Customize the chart appearance
        setupChart();

        addFeedingDataBtn.setOnClickListener(v -> showAddFeedingDataDialog());

        return view;
    }

    private void setupChart() {
        feedChart.getDescription().setEnabled(false);
        feedChart.setPinchZoom(false);
        feedChart.setDrawGridBackground(false);
        feedChart.setDrawBorders(false);

        // Customize X-axis
        feedChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        feedChart.getXAxis().setGranularity(1f);
        feedChart.getXAxis().setLabelCount(5);
        feedChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                return xLabels.get((int) value);
            }
        });

        // Customize Y-axis
        feedChart.getAxisLeft().setDrawGridLines(true);
        feedChart.getAxisLeft().setGranularity(20f); // Show Y-axis labels every 20kg
        feedChart.getAxisLeft().setTextColor(getResources().getColor(android.R.color.black));
        feedChart.getAxisLeft().setAxisMinimum(0f);
        feedChart.getAxisRight().setEnabled(false); // Disable right Y-axis
        feedChart.getLegend().setEnabled(false); // Disable legend
    }

    private void showAddFeedingDataDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_feed);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText feedTypeEdit = dialog.findViewById(R.id.editTextNumber);
        EditText amountInKgEdit = dialog.findViewById(R.id.editTextNumber3);
        Button applyChanges = dialog.findViewById(R.id.ApplyChangesBTN);
        Button datePickerButton = dialog.findViewById(R.id.datePickerBtn);

        // Initialize the date picker
        datePickerButton.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Feed Date")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build();
            datePicker.show(getParentFragmentManager(), "DATE_PICKER");
            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate = new Date(selection);
                datePickerButton.setText(dateFormat.format(selectedDate)); // Display selected date
            });
        });

        applyChanges.setOnClickListener(v -> {
            String feedType = feedTypeEdit.getText().toString().trim();
            String amountInKg = amountInKgEdit.getText().toString().trim();

            if (!feedType.isEmpty() && !amountInKg.isEmpty() && selectedDate != null) {
                currentFeedTypeTextView.setText(feedType);
                todaysValueTextView.setText(amountInKg + "kg");

                // Add data to the chart
                addDataToChart(Float.parseFloat(amountInKg), selectedDate);

                Toast.makeText(getActivity(), "Data Added: " + feedType + " - " + amountInKg + " on " + dateFormat.format(selectedDate), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Please fill all required fields and select a date.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void addDataToChart(float amount, Date date) {
        // Create a new entry for the chart
        entries.add(new BarEntry(entries.size(), amount)); // Each entry gets a unique x-value based on the size of the list
        xLabels.add(dateFormat.format(date)); // Add the selected date to the X-axis labels

        // Update the total feeds
        totalFeeds += amount; // Add the current amount to the total
        totalFeedsValueTextView.setText(String.format("%.2f kg", totalFeeds)); // Update the total feeds TextView

        // Create or update the dataset
        if (dataSet == null) {
            dataSet = new BarDataSet(entries, "Feeding Amounts");
            dataSet.setColor(getResources().getColor(R.color.teal_ish)); // Set bar color
            dataSet.setValueTextSize(12f); // Set the text size for values
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.5f); // Increase bar width for spacing
            feedChart.setData(barData);
        } else {
            dataSet.notifyDataSetChanged(); // Notify the dataset that the data has changed
            feedChart.getData().notifyDataChanged(); // Refresh the chart data
        }

        feedChart.notifyDataSetChanged(); // Notify the chart to update
        feedChart.invalidate(); // Refresh the chart
        feedChart.animateY(1000); // Optional: animate the chart
    }
}