package com.example.diyhub.Fragments;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.diyhub.R;

import java.util.Arrays;


public class SellerStatsPage extends AppCompatActivity {


    private XYPlot myPlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_stats_page);


        Number[] values1 = {80,33,65,83,52,40,23};
        Number[] values2 = {30,73,31,37,43,98,75};

        XYSeries series1 = new SimpleXYSeries(Arrays.asList(values1),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1");
        XYSeries series2 = new SimpleXYSeries(Arrays.asList(values2),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 2");

        BarFormatter formatter1 = new BarFormatter(Color.RED, Color.LTGRAY);
        BarFormatter formatter2 = new BarFormatter(Color.rgb(240,177,5), Color.YELLOW);

        myPlot.addSeries(series1, formatter1);
        myPlot.addSeries(series2, formatter2);




    }
}