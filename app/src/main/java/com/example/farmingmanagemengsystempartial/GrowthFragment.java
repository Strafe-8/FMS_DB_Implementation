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
    public interface OnGrowthDataChangedListener {
        void onGrowthDataChanged(float healthStatus);
    }

    private OnGrowthDataChangedListener callback;
    private Dialog dialog;
    private ImageView back;
    private Button addGrowthData;
    private Date initialUpdateDate;
    private static final String PREFS_NAME = "GrowthPrefs";
    private static final String KEY_INITIAL_DATE = "initialUpdateDate";
    private static final String KEY_TRACKING_START_DATE = "trackingStartDate";
    private static final String KEY_TOTAL_CHICKENS = "totalChickens";
    private Date selectedDate = null;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_growth, container, false);

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long initialMillis = prefs.getLong(KEY_INITIAL_DATE, -1);
        if (initialMillis != -1) {
            initialUpdateDate = new Date(initialMillis);
        }

// Add Firebase connection here to get the initial number of chickens
        // Example:
        // FirebaseFirestore db = FirebaseFirestore.getInstance();
        // db.collection("farms").document("farmId").get().addOnSuccessListener(documentSnapshot -> {
        //     if (documentSnapshot.exists()) {
        //         int totalChickens = documentSnapshot.getLong("totalChickens").intValue();
        //         prefs.edit().putInt(KEY_TOTAL_CHICKENS, totalChickens).apply();
        //         // Notify MortalityFragment of update here if needed
        //     }
        // });
        TextView sizeValString = view.findViewById(R.id.sizeValue);
        TextView dayValString = view.findViewById(R.id.daysOldValue);
        TextView averageWeightVal = view.findViewById(R.id.averageWeightValue);
        TextView targetValString = view.findViewById(R.id.targetValue);

        addGrowthData = view.findViewById(R.id.Add_growth_Data_Btn);
        addGrowthData.setOnClickListener(v ->
                showAddGrowthDataDialog(sizeValString, dayValString,
                        averageWeightVal, targetValString));

        back = view.findViewById(R.id.ReturnBtn);
        back.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), DashboardActivity.class));
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnGrowthDataChangedListener) {
            callback = (OnGrowthDataChangedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement OnGrowthDataChangedListener");
        }
    }

    private void showAddGrowthDataDialog(TextView sizeValString,
                                         TextView dayValString,
                                         TextView avgWeightValString,
                                         TextView targetValString) {
        dialog = new Dialog(requireActivity());
        dialog.setContentView(R.layout.dialog_add_growth);
        dialog.getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText sizeEdit = dialog.findViewById(R.id.editTextNumber);
        EditText sampleSizeEdit = dialog.findViewById(R.id.editTextNumber2);
        LinearLayout sampleSizeLayout = dialog.findViewById(R.id.sampleSizeLayout);
        Button generateSamplesButton = dialog.findViewById(R.id.generateSamplesButton);
        Button datePickerButton = dialog.findViewById(R.id.datePickerBtn);
        Button applyChanges = dialog.findViewById(R.id.ApplyChangesBTN);

        sampleSizeEdit.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    sampleSizeEdit.setError("Sample size cannot be empty");
                } else {
                    sampleSizeEdit.setError(null);
                }
            }
            public void afterTextChanged(Editable s) {}
        });

        generateSamplesButton.setOnClickListener(v -> {
            sampleSizeLayout.removeAllViews();
            try {
                int sampleSize = Integer.parseInt(sampleSizeEdit.getText().toString().trim());
                for (int i = 0; i < sampleSize; i++) {
                    EditText et = new EditText(requireActivity());
                    et.setHint("Weight of chicken " + (i + 1) + " in kg");
                    et.setInputType(android.text.InputType.TYPE_CLASS_NUMBER |
                            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    LinearLayout.LayoutParams lp =
                            new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                    et.setLayoutParams(lp);
                    sampleSizeLayout.addView(et);
                }
            } catch (NumberFormatException e) {
                sampleSizeEdit.setError("Enter a valid number");
            }
        });

        datePickerButton.setOnClickListener(v -> {
            MaterialDatePicker<Long> dp = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select start date")
                    .setPositiveButtonText("OK")
                    .setNegativeButtonText("Cancel")
                    .build();
            dp.show(getParentFragmentManager(), "DATE_PICKER");
            dp.addOnPositiveButtonClickListener(selection -> {
                selectedDate = new Date(selection);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selection);
                datePickerButton.setText(makeDateString(
                        c.get(Calendar.DAY_OF_MONTH),
                        c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.YEAR)));

                if (selectedDate.after(new Date())) {
                    Toast.makeText(getActivity(),
                            "Date cannot be in the future.", Toast.LENGTH_SHORT).show();
                    selectedDate = null;
                    return;
                }

                SharedPreferences prefs2 = requireActivity()
                        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                prefs2.edit()
                        .putLong(KEY_TRACKING_START_DATE, selectedDate.getTime())
                        .apply();
            });
        });

        applyChanges.setOnClickListener(v -> {
            String sizeStr = sizeEdit.getText().toString().trim();
            String sampleSizeStr = sampleSizeEdit.getText().toString().trim();
            if (sizeStr.isEmpty() || sampleSizeStr.isEmpty()) {
                Toast.makeText(getActivity(),
                        "Please fill required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            int sampleSize = Integer.parseInt(sampleSizeStr);
            float totalW = 0f;
            boolean valid = true;
            for (int i = 0; i < sampleSize; i++) {
                EditText et = (EditText) sampleSizeLayout.getChildAt(i);
                if (et == null || et.getText().toString().trim().isEmpty()) {
                    valid = false;
                    break;
                }
                totalW += Float.parseFloat(et.getText().toString().trim());
            }
            if (!valid) {
                Toast.makeText(getActivity(),
                        "Please fill all weight fields.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedDate == null) {
                Toast.makeText(getActivity(),
                        "Please select a valid start date", Toast.LENGTH_SHORT).show();
                return;
            }

            float avgWeight = totalW / sampleSize;
            float deviation = ((avgWeight - 3.0f) / 3.0f) * 100;

            if (initialUpdateDate == null) {
                initialUpdateDate = new Date();
                requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                        .edit()
                        .putLong(KEY_INITIAL_DATE, initialUpdateDate.getTime())
                        .apply();
            }

            SharedPreferences prefs3 = requireActivity()
                    .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            int totalChickens = Integer.parseInt(sizeStr);
            prefs3.edit()
                    .putInt(KEY_TOTAL_CHICKENS, totalChickens)
                    .apply();

            // Notify MortalityFragment if it's attached to the activity
            if (getActivity() instanceof DashboardActivity) {
                DashboardActivity activity = (DashboardActivity) getActivity();
                activity.updateMortalityFragment(totalChickens);
            }

            sizeValString.setText(sizeStr);
            dayValString.setText(String.valueOf(calculateDaysOld(selectedDate)));
            avgWeightValString.setText(String.format("%.2f/3.0kg", avgWeight));
            targetValString.setText(String.format("%.2f%%", deviation));

            if (callback != null) {
                callback.onGrowthDataChanged(deviation);
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private int calculateDaysOld(Date startDate) {
        long diff = new Date().getTime() - startDate.getTime();
        return (int)(diff / (1000 * 60 * 60 * 24)) + 1;
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
            default: return "JAN";
        }
    }
}