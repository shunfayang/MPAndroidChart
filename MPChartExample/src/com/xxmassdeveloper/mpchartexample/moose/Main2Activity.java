package com.xxmassdeveloper.mpchartexample.moose;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.xxmassdeveloper.mpchartexample.R;

public class Main2Activity extends AppCompatActivity {

    private LineChart mChartLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mChartLine = findViewById(R.id.line_chart);
        LineData mData = new LineData();
//        mData.
        mChartLine.setData(mData);
    }
}
