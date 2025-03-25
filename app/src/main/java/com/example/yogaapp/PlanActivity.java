package com.example.yogaapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlanActivity extends AppCompatActivity {

    private EditText etHeight, etWeight;
    private Button btnCalculate;
    private TextView tvResult;
    private LineChart lineChart;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "BMI_HISTORY";
    private static final int MAX_HISTORY = 10;
    private Button btnClearHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        btnClearHistory = findViewById(R.id.btn_clear_history);
        btnClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                if (lineChart.getData() != null) {
                    lineChart.getData().clearValues();
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }
        });



        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        btnCalculate = findViewById(R.id.btn_calculate);
        tvResult = findViewById(R.id.tv_result);
        lineChart = findViewById(R.id.line_chart);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBMI();
            }
        });

        loadChartData();
    }

    private void calculateBMI() {
        String heightStr = etHeight.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (heightStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please enter both height and weight!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float height = Float.parseFloat(heightStr) / 100;
            float weight = Float.parseFloat(weightStr);
            float bmi = weight / (height * height);

            String category;
            if (bmi < 18.5) category = "Underweight";
            else if (bmi < 24.9) category = "Normal weight";
            else if (bmi < 29.9) category = "Overweight";
            else category = "Obese";

            tvResult.setText(String.format("BMI: %.1f\nCategory: %s", bmi, category));

            saveBMI(bmi);
            loadChartData();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input! Please enter valid numbers.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBMI(float bmi) {
        List<String> bmiList = new ArrayList<>(Arrays.asList(getBMIHistory()));
        if (bmiList.size() >= MAX_HISTORY) bmiList.remove(0);
        bmiList.add(String.valueOf(bmi));

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("bmi_data", String.join(",", bmiList));
        editor.apply();
    }

    private String[] getBMIHistory() {
        String data = sharedPreferences.getString("bmi_data", "");
        return data.isEmpty() ? new String[0] : data.split(",");
    }

    private void loadChartData() {
        String[] bmiHistory = getBMIHistory();
        List<Entry> entries = new ArrayList<>();

        for (int i = 0; i < bmiHistory.length; i++) {
            entries.add(new Entry(i + 1, Float.parseFloat(bmiHistory[i])));
        }

        LineDataSet dataSet = new LineDataSet(entries, "BMI History");
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextSize(12f);
        dataSet.setCircleColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);

        lineChart.getAxisRight().setEnabled(false);
        lineChart.invalidate();
    }
}
