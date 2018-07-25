
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xxmassdeveloper.mpchartexample.custom.MyCustomXAxisValueFormatter;
import com.xxmassdeveloper.mpchartexample.model.MockData;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LineChartActivity2 extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private LineChart mChart, mChart2;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private String mockData;
    private ArrayList<Entry> yVals1;
    private ArrayList<Entry> yVals2;
    private ArrayList<Entry> yVals3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        mockData = getJsonData();
        initArrayList(200, 400);
        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);
        mSeekBarX = findViewById(R.id.seekBar1);
        mSeekBarY = findViewById(R.id.seekBar2);

        mSeekBarX.setProgress(45);
        mSeekBarY.setProgress(100);

        mSeekBarY.setOnSeekBarChangeListener(this);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mChart = findViewById(R.id.chart1);
        mChart2 = findViewById(R.id.chart2);
        setChart(mChart);
        setChart(mChart2);



        // set an alternative background color
//        mChart.setBackgroundColor(Color.Whi);

        // add data
        setData(200, 400, mChart);
        setData(200, 400, mChart2);

//        mChart.animateX(2500);// 动画
        setLegend(mChart);
        setLegend(mChart2);


        setAxis(mChart);
        setAxis(mChart2);
    }

    private void setAxis(LineChart chart) {
        int range = 400;
        // X 轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        int grayTransparent = 0x22222222;
        xAxis.setTextColor(grayTransparent);
        xAxis.setLabelCount(5, true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(grayTransparent);

        // 左边 Y 轴
        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(0xff317DEC);
        leftAxis.setAxisMaximum(range);
        leftAxis.setAxisMinimum(range/4);
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisLineColor(grayTransparent);
        leftAxis.setGridColor(grayTransparent);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setGranularity(1f);
        leftAxis.setCenterAxisLabels(false);
        leftAxis.setLabelCount(5, true);
        leftAxis.setmDrawBottomYLabelEntry(false);
        leftAxis.setDrawTopYLabelEntry(false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        // 右边 Y 轴
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setAxisLineColor(grayTransparent);
        rightAxis.setTextColor(0xff009B8B);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(0);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(true);
        rightAxis.setGranularity(1f);
        rightAxis.setCenterAxisLabels(false);
        rightAxis.setLabelCount(5, true);
        rightAxis.setDrawTopYLabelEntry(false);
        rightAxis.setmDrawBottomYLabelEntry(false);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "$" + value;
            }
        });
    }

    private void setLegend(LineChart chart) {
        // 底部那个叫啥我忘了，表示哪根线代表啥，那根线又代表啥的用处——貌似叫图例
        // get the legend (only possible after setting data)
        Legend l = chart.getLegend(); // modify the legend ...
        l.setForm(LegendForm.SQUARE);
        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setTextColor(0xff222222);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYEntrySpace(200f);// 在垂直轴(以像素为单位)上设置图例条目之间的空间
        l.setXEntrySpace(getResources().getDimension(R.dimen.space_12sp));
        l.setFormToTextSpace(5f);
//        l.setStackSpace(20f);
//        l.setYOffset(12f);// 相当于 bottom padding
//        l.setFormSize(100f);
//        l.setFormLineWidth(10f);
    }

    private void setChart(LineChart chart) {
        chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {

                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.CUBIC_BEZIER);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.STEPPED);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = mChart.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                mChart.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChart.isPinchZoomEnabled())
                    mChart.setPinchZoom(false);
                else
                    mChart.setPinchZoom(true);

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChart.setAutoScaleMinMaxEnabled(!mChart.isAutoScaleMinMaxEnabled());
                mChart.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
                //mChart.highlightValue(9.7f, 1, false);
                break;
            }
            case R.id.animateY: {
                mChart.animateY(3000);
                break;
            }
            case R.id.animateXY: {
                mChart.animateXY(3000, 3000);
                break;
            }

            case R.id.actionSave: {
                if (mChart.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                // mChart.saveToGallery("title"+System.currentTimeMillis())
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress(), mChart);

        // redraw
        mChart.invalidate();
    }

    private void setData(int count, float range, LineChart chart) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(yVals1);
//            set2.setValues(yVals2);
//            set3.setValues(yVals3);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals1, "DataSet 1");

            set1.setAxisDependency(AxisDependency.LEFT);
//            set1.setColor(ColorTemplate.getHoloBlue());
//            set1.setCircleColor(Color.WHITE);
//            set1.setLineWidth(2f);
//            set1.setCircleRadius(3f);
//            set1.setFillAlpha(65);
//            set1.setFillColor(ColorTemplate.getHoloBlue());
//            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);

            set1.setDrawIcons(false);
            set1.setColor(0xff859795);
            set1.setLineWidth(2f);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setFormSize(15.f);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.set1));
            set1.setLabel("BTC价格");

            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

//            // create a dataset and give it a type
//            set2 = new LineDataSet(yVals2, "DataSet 2");
//            set2.setAxisDependency(AxisDependency.RIGHT);
////            set2.setColor(Color.RED);
////            set2.setCircleColor(Color.WHITE);
////            set2.setLineWidth(2f);
////            set2.setCircleRadius(3f);
////            set2.setFillAlpha(65);
////            set2.setFillColor(Color.RED);
////            set2.setDrawCircleHole(false);
////            set2.setHighLightColor(Color.rgb(244, 117, 117));
////            //set2.setFillFormatter(new MyFillFormatter(900f));
//
//            set2.setDrawIcons(false);
//            set2.setColor(0xff27AB9D);
//            set2.setLineWidth(2f);
//            set2.setValueTextSize(9f);
//            set2.setDrawFilled(false);
//            set2.setFormLineWidth(1f);
//            set2.setDrawCircles(false);
//            set2.setFormSize(15.f);
//
//            set3 = new LineDataSet(yVals3, "DataSet 3");
//            set3.setAxisDependency(AxisDependency.RIGHT);
////            set3.setColor(Color.YELLOW);
////            set3.setCircleColor(Color.WHITE);
////            set3.setLineWidth(2f);
////            set3.setCircleRadius(3f);
////            set3.setFillAlpha(65);
////            set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
////            set3.setDrawCircleHole(false);
////            set3.setHighLightColor(Color.rgb(244, 117, 117));
//            set3.setDrawIcons(false);
//            set3.setColor(Color.BLUE);
//            set3.setLineWidth(2f);
//            set3.setValueTextSize(9f);
//            set3.setDrawFilled(false);
//            set3.setFormLineWidth(0.1f);
//            set3.setDrawCircles(false);
//            set3.setFormSize(15.f);
//            set2.setLabel("美元价格");
//            set3.setLabel("24h小时成交");
//
//
//            set2.setMode(LineDataSet.Mode.CUBIC_BEZIER);
//            set3.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // create a data object with the datasets
            LineData data = new LineData(set1/*, set2, set3*/);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

    private void initArrayList(int count, float range) {
        yVals1 = new ArrayList<Entry>();
        yVals2 = new ArrayList<Entry>();
        yVals3 = new ArrayList<Entry>();
        if (mockData.length() > 0){
//            for ()
            Gson gson = new Gson();
            Type type = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> list = gson.fromJson(mockData, type);
            for (int i = 0; i<list.size(); i++){
                List<String> values = list.get(i);
                int size = values.size();
                Double time = Double.parseDouble(values.get(0).toString());
                Double btcPrice = Double.parseDouble(values.get(1).toString());
                Double usdPrice = Double.parseDouble(values.get(2).toString());
                Double cap = Double.parseDouble(values.get(3).toString());
                Double volume = Double.parseDouble(values.get(4).toString());
                yVals1.add(new Entry(i, btcPrice.floatValue()));
                yVals2.add(new Entry(i, usdPrice.floatValue()));
                yVals3.add(new Entry(i, cap.floatValue()));
            }
        } else {
            for (int i = 0; i < count; i++) {
                float mult = range / 2f;
                float val = (float) (Math.random() * 100) + mult;
                yVals1.add(new Entry(i, val));
            }

            for (int i = 0; i < count; i++) {
                float mult = range;
                float val = (float) (Math.random() * mult) + 450;
                yVals2.add(new Entry(i, val));
            }
            for (int i = 0; i < count; i++) {
                float mult = range;
                float val = (float) (Math.random() * mult) + 500;
                yVals3.add(new Entry(i, val));
            }
        }
    }

    @NonNull
    private String getJsonData() {
        BufferedReader reader = null;
        String mockData = "";
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("data.TXT")));
            StringBuilder sb = new StringBuilder();
            String temp = "";
            while( (temp = reader.readLine())!= null){
                sb.append(temp);
            }
            mockData = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            if (reader != null){
                try {
                    reader.close();
                    reader = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                    reader = null;
                }
            }
        }
        return mockData;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        mChart.centerViewToAnimated(e.getX(), e.getY(), mChart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
        //mChart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //mChart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }
}
