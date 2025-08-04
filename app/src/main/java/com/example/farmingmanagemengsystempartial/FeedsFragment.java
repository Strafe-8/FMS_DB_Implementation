package com.example.farmingmanagemengsystempartial;

import android.app.Dialog;
import android.content.Context;
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
    public interface OnFeedDataChangedListener {
        void onFeedDataChanged(float totalFeeds);
    }

    private OnFeedDataChangedListener callback;

    private Dialog dialog;
    private Button addFeedingDataBtn;
    private TextView currentFeedTypeTextView;
    private TextView todaysValueTextView;
    private TextView totalFeedsValueTextView;
    private BarChart feedChart;

    private ArrayList<BarEntry> entries = new ArrayList<>();
    private ArrayList<String> xLabels = new ArrayList<>();
    private BarDataSet dataSet;
    private float totalFeeds = 0;
    private Date selectedDate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        addFeedingDataBtn = view.findViewById(R.id.Add_Feeding_Data_Btn);
        currentFeedTypeTextView = view.findViewById(R.id.CurrentFeedTypes);
        todaysValueTextView = view.findViewById(R.id.TodaysValue);
        totalFeedsValueTextView = view.findViewById(R.id.totalFeedsValue);
        feedChart = view.findViewById(R.id.feed_chart);

        setupChart();
        addFeedingDataBtn.setOnClickListener(v -> showAddFeedingDataDialog());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFeedDataChangedListener) {
            callback = (OnFeedDataChangedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnFeedDataChangedListener");
        }
    }

    private void setupChart() {
        feedChart.getDescription().setEnabled(false);
        feedChart.setPinchZoom(false);
        feedChart.setDrawGridBackground(false);
        feedChart.setDrawBorders(false);

        feedChart.getXAxis().setPosition(com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM);
        feedChart.getXAxis().setGranularity(1f);
        feedChart.getXAxis().setLabelCount(5);
        feedChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, com.github.mikephil.charting.components.AxisBase axis) {
                if ((int) value >= 0 && (int) value < xLabels.size()) {
                    return xLabels.get((int) value);
                } else {
                    return "";
                }
            }
        });

        feedChart.getAxisLeft().setDrawGridLines(true);
        feedChart.getAxisLeft().setGranularity(20f);
        feedChart.getAxisLeft().setAxisMinimum(0f);
        feedChart.getAxisRight().setEnabled(false);
        feedChart.getLegend().setEnabled(false);
    }

    private void showAddFeedingDataDialog() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_feed);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText feedTypeEdit = dialog.findViewById(R.id.editTextNumber);
        EditText amountInKgEdit = dialog.findViewById(R.id.editTextNumber3);
        Button applyChanges = dialog.findViewById(R.id.ApplyChangesBTN);
        Button datePickerButton = dialog.findViewById(R.id.datePickerBtn);

        datePickerButton.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select Feed Date")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build();

            datePicker.show(getParentFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDate = new Date(selection);
                datePickerButton.setText(dateFormat.format(selectedDate));
            });
        });

        applyChanges.setOnClickListener(v -> {
            String feedType = feedTypeEdit.getText().toString().trim();
            String amountInKg = amountInKgEdit.getText().toString().trim();

            if (!feedType.isEmpty() && !amountInKg.isEmpty() && selectedDate != null) {
                currentFeedTypeTextView.setText(feedType);
                todaysValueTextView.setText(amountInKg + "kg");

                addDataToChart(Float.parseFloat(amountInKg), selectedDate);

                if (callback != null) {
                    callback.onFeedDataChanged(totalFeeds);
                }

                Toast.makeText(getActivity(), "Data Added: " + feedType + " - " + amountInKg, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Please fill all fields and select a date.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void addDataToChart(float amount, Date date) {
        entries.add(new BarEntry(entries.size(), amount));
        xLabels.add(dateFormat.format(date));

        totalFeeds += amount;
        totalFeedsValueTextView.setText(String.format(Locale.getDefault(), "%.2f kg", totalFeeds));

        if (dataSet == null) {
            dataSet = new BarDataSet(entries, "Feeding Amounts");
            dataSet.setColor(getResources().getColor(R.color.teal_ish));
            dataSet.setValueTextSize(12f);
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.5f);
            feedChart.setData(barData);
        } else {
            dataSet.notifyDataSetChanged();
            feedChart.getData().notifyDataChanged();
        }

        feedChart.notifyDataSetChanged();
        feedChart.invalidate();
        feedChart.animateY(1000);
    }
}
