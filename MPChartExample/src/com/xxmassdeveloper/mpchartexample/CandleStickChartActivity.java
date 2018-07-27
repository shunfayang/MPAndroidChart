
package com.xxmassdeveloper.mpchartexample;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.YAxis.AxisDependency;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.matrix.Vector3;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xxmassdeveloper.mpchartexample.moose.JsonUtils;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CandleStickChartActivity extends DemoBase implements OnSeekBarChangeListener {

    private CandleStickChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    private String mockData;
    private ArrayList<CandleEntry> usdPriceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_candlechart);

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        mSeekBarX = findViewById(R.id.seekBar1);
        mSeekBarX.setOnSeekBarChangeListener(this);

        mSeekBarY = findViewById(R.id.seekBar2);
        mSeekBarY.setOnSeekBarChangeListener(this);

        //
        mockData = JsonUtils.getJsonData(this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<List<String>>>() {}.getType();
        List<List<String>> list = gson.fromJson(mockData, type);
        usdPriceList = new ArrayList<CandleEntry>();
        for (int i = 0; i<100; i++) {
            List<String> values = list.get(i);
            Double btcprice = Double.parseDouble(values.get(1));
//            usdPriceList.add(new Entry(i, btcprice.floatValue()));
            float v = btcprice.floatValue();
            float close = 10f;
            if (i < 10){
                close = -i;
            }
            if (i < 20 && i> 10){
                close = i;
            }
            if (i < 30 && i> 20){
                close = i;
            }
            if (i < 40 && i> 30){
                close = -i;
            }
            if (i < 100 && i> 40){
                close = -i;
            }
            usdPriceList.add(new CandleEntry(i, v + 10,v- 10,v-close, v+close));
        }
        //

        mChart = findViewById(R.id.chart1);
        mChart.setBackgroundColor(Color.WHITE);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(10);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(true);
        mChart.setScaleYEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setDragYEnabled(false);

        mChart.setDrawGridBackground(false);
//        mChart.setVisibleXRange(0f, 10f);
//        mChart.setVisibleXRangeMaximum();
        mChart.setMaxVisibleValueCount(20);
        mChart.setScaleMinima(4f, 1f);
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();
        viewPortHandler.setMinMaxScaleX(1f, 4f);
        viewPortHandler.setDragOffsetX(100f);



        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setLabelCount(5, true);

        YAxis leftAxis = mChart.getAxisLeft();  
//        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(5, true);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(true);

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setEnabled(true);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setDrawGridLines(false);
//        rightAxis.setStartAtZero(false);

        // setting data
        mSeekBarX.setProgress(200);
        mSeekBarY.setProgress(100);
        
        mChart.getLegend().setEnabled(false);
        mChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                float chartWidth = mChart.getViewPortHandler().getChartWidth();
                mChart.moveViewToX(chartWidth);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.candle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionToggleValues: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleIcons: {
                for (IDataSet set : mChart.getData().getDataSets())
                    set.setDrawIcons(!set.isDrawIconsEnabled());

                mChart.invalidate();
                break;
            }
            case R.id.actionToggleHighlight: {
                if(mChart.getData() != null) {
                    mChart.getData().setHighlightEnabled(!mChart.getData().isHighlightEnabled());
                    mChart.invalidate();
                }
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
            case R.id.actionToggleMakeShadowSameColorAsCandle: {
                for (ICandleDataSet set : mChart.getData().getDataSets()) {
                   //TODO: set.setShadowColorSameAsCandle(!set.getShadowColorSameAsCandle());
                }

                mChart.invalidate();
                break;
            }
            case R.id.animateX: {
                mChart.animateX(3000);
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
                if (mChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {
                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
        }
        return true;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        
        int prog = (mSeekBarX.getProgress() + 1);

        tvX.setText("" + prog);
        tvY.setText("" + (mSeekBarY.getProgress()));
        
        mChart.resetTracking();

        ArrayList<CandleEntry> yVals1 = new ArrayList<CandleEntry>();

        for (int i = 0; i < prog; i++) {
            float mult = (mSeekBarY.getProgress() + 1);
            float val = (float) (Math.random() * 40) + mult;
            
            float high = (float) (Math.random() * 2) + 2f;
            float low = (float) (Math.random() * 2) + 2f;
            
            float open = (float) (Math.random() * 4) + 1f;
            float close = (float) (Math.random() * 4) + 1f;

            boolean even = i % 2 == 0;

            yVals1.add(new CandleEntry(
                    i, val + high,
                    val - low,
                    even ? val + open : val - open,
                    even ? val - close : val + close,
                    getResources().getDrawable(R.drawable.star)
            ));
        }

        CandleDataSet set1 = new CandleDataSet(yVals1, "Data Set");
//        CandleDataSet set1 = new CandleDataSet(usdPriceList, "Data Set");

        set1.setDrawIcons(false);
//        set1.setAxisDependency(AxisDependency.LEFT);
//        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.5f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);
        set1.setDrawHorizontalHighlightIndicator(true);
        set1.setAxisDependency(AxisDependency.RIGHT);
        //set1.setHighlightLineWidth(1f);

        CandleData data = new CandleData(set1);
        ViewPortHandler viewPortHandler = mChart.getViewPortHandler();
//        viewPortHandler.translate(new float[]{viewPortHandler.getChartWidth(), 0f});
        mChart.setData(data);
//        mChart.invalidate();
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
