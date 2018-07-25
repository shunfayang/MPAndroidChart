
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxmassdeveloper.mpchartexample.custom.MyMarkerView;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LineChartActivity2 extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private LineChart mChartTradeCount, mChartBtcPrice, mChartVolume, mChartUsdPrice;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private String mockData;
    private ArrayList<Entry> usdPrice;
    private ArrayList<Entry> btcPrice;
    private ArrayList<Entry> volume;
    private ArrayList<Entry> tradeCount;
    private ArrayList<Entry> yVals5;

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


        mChartBtcPrice = findViewById(R.id.chart2);
        setChart(mChartBtcPrice);
        setChart2Data(200, 400, mChartBtcPrice, btcPrice);
        setLegend(mChartBtcPrice);
        setAxis(mChartBtcPrice, 0.08f, 0.07f);// 不能写死
        mChartBtcPrice.getAxisLeft().setDrawLabels(false);

        mChartVolume = findViewById(R.id.chart3);
        setChart(mChartVolume);
        setChart3Data(200, 400, mChartVolume, volume);
        setLegend(mChartVolume);
        setAxis(mChartVolume, Double.valueOf("4.8426103E10").floatValue()*1.1f, Double.valueOf("4.8426103E10").floatValue()*0.9f);
        mChartVolume.getAxisRight().setDrawLabels(false);
        mChartVolume.getAxisLeft().setDrawLabels(false);

        mChartUsdPrice = findViewById(R.id.chart4);
        setChart(mChartUsdPrice);
        setChart4Data(200, 400, mChartUsdPrice, usdPrice);
        setLegend(mChartUsdPrice);
        setAxis(mChartUsdPrice, 500, 470);// 不能写死
//        mChartUsdPrice.getAxisLeft().setDrawLabels(false);
//        mChartUsdPrice.getAxisRight().setDrawLabels(false);

        // 底部填充的视图
        mChartTradeCount = findViewById(R.id.chart1);
        setChart(mChartTradeCount);
        setChart1Data(200, 400, mChartTradeCount, tradeCount);
        setLegend(mChartTradeCount);
        setAxis(mChartTradeCount, Double.valueOf("1.63532902E9").floatValue()*1.1f, Double.valueOf("1.63532902E9").floatValue()*0.5f);
        mChartTradeCount.getAxisRight().setDrawLabels(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(mChartUsdPrice); // For bounds control
        mChartUsdPrice.setMarker(mv); // Set the marker to the chart
    }

    private void setAxis(LineChart chart,float max, float min) {
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
        leftAxis.setAxisMaximum(max);
        leftAxis.setAxisMinimum(min);
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
        rightAxis.setAxisMaximum(max);
        rightAxis.setAxisMinimum(min);
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
//        l.setEnabled(false);
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
        chart.setBackgroundColor(Color.TRANSPARENT);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);
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
                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setDrawValues(!set.isDrawValuesEnabled());
                }

                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if (mChartTradeCount.getData() != null) {
                    mChartTradeCount.getData().setHighlightEnabled(!mChartTradeCount.getData().isHighlightEnabled());
                    mChartTradeCount.invalidate();
                }
                break;
            }
            case R.id.actionToggleFilled: {

                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawFilledEnabled())
                        set.setDrawFilled(false);
                    else
                        set.setDrawFilled(true);
                }
                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleCircles: {
                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    if (set.isDrawCirclesEnabled())
                        set.setDrawCircles(false);
                    else
                        set.setDrawCircles(true);
                }
                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleCubic: {
                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.CUBIC_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.CUBIC_BEZIER);
                }
                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleStepped: {
                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.STEPPED
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.STEPPED);
                }
                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleHorizontalCubic: {
                List<ILineDataSet> sets = mChartTradeCount.getData()
                        .getDataSets();

                for (ILineDataSet iSet : sets) {

                    LineDataSet set = (LineDataSet) iSet;
                    set.setMode(set.getMode() == LineDataSet.Mode.HORIZONTAL_BEZIER
                            ? LineDataSet.Mode.LINEAR
                            : LineDataSet.Mode.HORIZONTAL_BEZIER);
                }
                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionTogglePinch: {
                if (mChartTradeCount.isPinchZoomEnabled())
                    mChartTradeCount.setPinchZoom(false);
                else
                    mChartTradeCount.setPinchZoom(true);

                mChartTradeCount.invalidate();
                break;
            }
            case R.id.actionToggleAutoScaleMinMax: {
                mChartTradeCount.setAutoScaleMinMaxEnabled(!mChartTradeCount.isAutoScaleMinMaxEnabled());
                mChartTradeCount.notifyDataSetChanged();
                break;
            }
            case R.id.animateX: {
                mChartTradeCount.animateX(3000);
                //mChartTradeCount.highlightValue(9.7f, 1, false);
                break;
            }
            case R.id.animateY: {
                mChartTradeCount.animateY(3000);
                break;
            }
            case R.id.animateXY: {
                mChartTradeCount.animateXY(3000, 3000);
                break;
            }

            case R.id.actionSave: {
                if (mChartTradeCount.saveToPath("title" + System.currentTimeMillis(), "")) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();

                // mChartTradeCount.saveToGallery("title"+System.currentTimeMillis())
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tvX.setText("" + (mSeekBarX.getProgress() + 1));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setChart1Data(mSeekBarX.getProgress() + 1, mSeekBarY.getProgress(), mChartTradeCount, usdPrice);

        // redraw
        mChartTradeCount.invalidate();
    }

    private void setChart1Data(int count, float range, LineChart chart, ArrayList<Entry> list) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(list);
//            set2.setValues(btcPrice);
//            set3.setValues(volume);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(list, "DataSet 1");
            set1.setAxisDependency(AxisDependency.LEFT);
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
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }
    private void setChart3Data(int count, float range, LineChart chart, ArrayList<Entry> list) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(list);
//            set2.setValues(btcPrice);
//            set3.setValues(volume);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(list, "DataSet 1");
            set1.setAxisDependency(AxisDependency.LEFT);
            set1.setDrawIcons(false);
            set1.setColor(0xff317DEC);
            set1.setLineWidth(2f);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            set1.setFormLineWidth(1f);
            set1.setDrawCircles(false);
            set1.setFormSize(15.f);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.set1));
            set1.setLabel("BTC价格");
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }
    private void setChart2Data(int count, float range, LineChart chart, ArrayList<Entry> list) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(list);
//            set2.setValues(btcPrice);
//            set3.setValues(volume);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(list, "美元价格");
            set1.setDrawIcons(false);
            set1.setLineWidth(2f);
            set1.setValueTextSize(9f);
            set1.setFormLineWidth(1f);
            set1.setFormSize(15.f);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.set1));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawCircles(false);
            set1.setAxisDependency(AxisDependency.RIGHT);

            set1.setColor(0xffF08422);
            set1.setDrawFilled(false);
            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

    private void setChart4Data(int count, float range, LineChart chart, ArrayList<Entry> list) {
        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
//            set2 = (LineDataSet) chart.getData().getDataSetByIndex(1);
//            set3 = (LineDataSet) chart.getData().getDataSetByIndex(2);
            set1.setValues(list);
//            set2.setValues(btcPrice);
//            set3.setValues(volume);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(list, "美元价格");
            set1.setDrawIcons(false);
            set1.setLineWidth(2f);
            set1.setValueTextSize(9f);
            set1.setFormLineWidth(1f);
            set1.setFormSize(15.f);
            set1.setFillDrawable(getResources().getDrawable(R.drawable.set1));
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setDrawCircles(false);
            set1.setAxisDependency(AxisDependency.RIGHT);

            set1.setColor(0xff009B8B);
            set1.setDrawFilled(false);
            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

    private void initArrayList(int count, float range) {
        usdPrice = new ArrayList<Entry>();
        btcPrice = new ArrayList<Entry>();
        volume = new ArrayList<Entry>();
        tradeCount = new ArrayList<Entry>();
        if (mockData.length() > 0){
//            for ()
            Gson gson = new Gson();
            Type type = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> list = gson.fromJson(mockData, type);
            for (int i = 0; i<list.size(); i++){
                List<String> values = list.get(i);
//                Double time = Long.parseLong(values.get(0));
                Double usdprice = Double.parseDouble(values.get(1));
                Double btcprice = Double.parseDouble(values.get(2));
                Double volume = Double.parseDouble(values.get(3));
                Double tradeCount = Double.parseDouble(values.get(4));

                usdPrice.add(new Entry(i, usdprice.floatValue()));
                btcPrice.add(new Entry(i, btcprice.floatValue()));
                this.volume.add(new Entry(i, volume.floatValue()));
                this.tradeCount.add(new Entry(i, tradeCount.floatValue()));
            }
        } else {
            for (int i = 0; i < count; i++) {
                float mult = range / 2f;
                float val = (float) (Math.random() * 100) + mult;
                usdPrice.add(new Entry(i, val));
            }

            for (int i = 0; i < count; i++) {
                float mult = range;
                float val = (float) (Math.random() * mult) + 450;
                btcPrice.add(new Entry(i, val));
            }
            for (int i = 0; i < count; i++) {
                float mult = range;
                float val = (float) (Math.random() * mult) + 500;
                volume.add(new Entry(i, val));
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

        mChartTradeCount.centerViewToAnimated(e.getX(), e.getY(), mChartTradeCount.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
        //mChartTradeCount.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChartTradeCount.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //mChartTradeCount.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChartTradeCount.getData().getDataSetByIndex(dataSetIndex)
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
