package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.diyhub.Notifications.Data;
import com.example.diyhub.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SellerStatsFragment extends Fragment {


    View view;
    BarChart barChartOrders,barchartSales,barchartShopViews;
    CardView ordersCardview,salesCardview,shopViewsCardview;
    List<SellerStatsOrders> sellerStatsOrdersList;
    List<SellerStatsSales> sellerStatsSalesList;
    List<SellerStatsShopViews> sellerStatsShopViewsList;


    ProgressDialog dialogOrders,dialogSales,dialogShopViews;
    TextView totalShopViewsTxt,totalSalesTxt,totalOrdersTxt;
    FirebaseUser user;

    CardView alertMessageCardView;
    TextView avgOrdersTxt,avgSalesTxt,avgShopViewsTxt;


    public SellerStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_seller_stats, container, false);

        barChartOrders = view.findViewById(R.id.bar_chartOrders);
        barchartSales = view.findViewById(R.id.bar_chartSales);
        barchartShopViews = view.findViewById(R.id.bar_chartShopViews);
        ordersCardview = view.findViewById(R.id.ordersCardView);
        salesCardview  = view.findViewById(R.id.salesCardView);
        shopViewsCardview = view.findViewById(R.id.shopViewsCardView);
        totalShopViewsTxt = view.findViewById(R.id.totalShopViews);
        totalSalesTxt = view.findViewById(R.id.totalSalesTxt);
        totalOrdersTxt = view.findViewById(R.id.totalOrdersTxt);
        alertMessageCardView = view.findViewById(R.id.alertMessageCardView);
        avgOrdersTxt = view.findViewById(R.id.avgTotalOrders);
        avgSalesTxt = view.findViewById(R.id.avgTotalSales);
        avgShopViewsTxt = view.findViewById(R.id.avgTotalShopViews);


        user = FirebaseAuth.getInstance().getCurrentUser();







        //barChart.groupBars(0,groupSpace, barSpace);


        ordersCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersCardview.setContentPadding(3,3,3,3);
                ordersCardview.setCardBackgroundColor(Color.RED);
                salesCardview.setContentPadding(0,0,0,0);
                salesCardview.setCardBackgroundColor(Color.WHITE);
                shopViewsCardview.setContentPadding(0,0,0,0);
                shopViewsCardview.setCardBackgroundColor(Color.WHITE);


                barChartOrders.setVisibility(View.VISIBLE);
                barchartSales.setVisibility(View.INVISIBLE);
                barchartShopViews.setVisibility(View.INVISIBLE);

                viewOrders();




            }
        });
        salesCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salesCardview.setContentPadding(3,3,3,3);
                salesCardview.setCardBackgroundColor(Color.RED);
                ordersCardview.setContentPadding(0,0,0,0);
                ordersCardview.setCardBackgroundColor(Color.WHITE);
                shopViewsCardview.setContentPadding(0,0,0,0);
                shopViewsCardview.setCardBackgroundColor(Color.WHITE);

                barChartOrders.setVisibility(View.INVISIBLE);
                barchartSales.setVisibility(View.VISIBLE);
                barchartShopViews.setVisibility(View.INVISIBLE);

                viewSales();


            }
        });
        shopViewsCardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shopViewsCardview.setContentPadding(3,3,3,3);
                shopViewsCardview.setCardBackgroundColor(Color.RED);
                ordersCardview.setContentPadding(0,0,0,0);
                ordersCardview.setCardBackgroundColor(Color.WHITE);
                salesCardview.setContentPadding(0,0,0,0);
                salesCardview.setCardBackgroundColor(Color.WHITE);

                barChartOrders.setVisibility(View.INVISIBLE);
                barchartSales.setVisibility(View.INVISIBLE);
                barchartShopViews.setVisibility(View.VISIBLE);


                viewShopViews();


            }
        });

        return view;
    }
    public void viewOrders()
    {
        sellerStatsOrdersList = new ArrayList<>();
        Legend legend = barChartOrders.getLegend();
        legend.setTextSize(20f);
        barChartOrders.setDrawBarShadow(false);
        barChartOrders.setDrawValueAboveBar(true);
        barChartOrders.setMaxVisibleValueCount(100);
        barChartOrders.setPinchZoom(true);
        barChartOrders.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stats").child(user.getUid()).child("2022-Orders");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerStatsOrdersList.clear();
                barEntries.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SellerStatsOrders orders = snapshot.getValue(SellerStatsOrders.class);
                    sellerStatsOrdersList.add(orders);

                }
                barEntries.add(new BarEntry(1,sellerStatsOrdersList.get(0).getJan()));
                barEntries.add(new BarEntry(2,sellerStatsOrdersList.get(0).getFeb()));
                barEntries.add(new BarEntry(3,sellerStatsOrdersList.get(0).getMar()));
                barEntries.add(new BarEntry(4,sellerStatsOrdersList.get(0).getApr()));
                barEntries.add(new BarEntry(5,sellerStatsOrdersList.get(0).getMay()));
                barEntries.add(new BarEntry(6,sellerStatsOrdersList.get(0).getJun()));
                barEntries.add(new BarEntry(7,sellerStatsOrdersList.get(0).getJul()));
                barEntries.add(new BarEntry(8,sellerStatsOrdersList.get(0).getAug()));
                barEntries.add(new BarEntry(9,sellerStatsOrdersList.get(0).getSep()));
                barEntries.add(new BarEntry(10,sellerStatsOrdersList.get(0).getOct()));
                barEntries.add(new BarEntry(11,sellerStatsOrdersList.get(0).getNov()));
                barEntries.add(new BarEntry(12,sellerStatsOrdersList.get(0).getDec()));

                int[] totaOrders = new int[]{sellerStatsOrdersList.get(0).getJan(),
                        sellerStatsOrdersList.get(0).getFeb(),
                        sellerStatsOrdersList.get(0).getMar(),
                        sellerStatsOrdersList.get(0).getApr(),
                        sellerStatsOrdersList.get(0).getMay(),
                        sellerStatsOrdersList.get(0).getJun(),
                        sellerStatsOrdersList.get(0).getJul(),
                        sellerStatsOrdersList.get(0).getAug(),
                        sellerStatsOrdersList.get(0).getSep(),
                        sellerStatsOrdersList.get(0).getOct(),
                        sellerStatsOrdersList.get(0).getNov(),
                        sellerStatsOrdersList.get(0).getDec()};
                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                int finalTotal = 0;
                for(int i =0; i<12; i++)
                {
                    finalTotal = totaOrders[i] + finalTotal;
                }
                totalOrdersTxt.setText(String.valueOf(myFormat.format(finalTotal)));
                avgOrdersTxt.setText(String.valueOf(myFormat.format(finalTotal/12))+" - AVG");

                BarDataSet barDataSet = new BarDataSet(barEntries, "Orders");
                barDataSet.setColor(Color.RED);
                BarData data = new BarData(barDataSet);
                barChartOrders.setData(data);
                data.setBarWidth(0.50f);

                String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                XAxis xAxis = barChartOrders.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);

                barChartOrders.setDragEnabled(true);

                float barSpace = 0.08f;
                float groupSpace = 0.44f;

                barChartOrders.getXAxis().setAxisMinimum(0.5f);
                //barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*12);
                barChartOrders.getAxisLeft().setAxisMinimum(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void viewSales()
    {
        sellerStatsSalesList = new ArrayList<>();
        Legend legend = barchartSales.getLegend();
        legend.setTextSize(20f);
        barchartSales.setDrawBarShadow(false);
        barchartSales.setDrawValueAboveBar(true);
        barchartSales.setMaxVisibleValueCount(100);
        barchartSales.setPinchZoom(true);
        barchartSales.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stats").child(user.getUid()).child("2022-Sales");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerStatsSalesList.clear();
                barEntries.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SellerStatsSales orders = snapshot.getValue(SellerStatsSales.class);
                    sellerStatsSalesList.add(orders);

                }
                barEntries.add(new BarEntry(1,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getJan()))));
                barEntries.add(new BarEntry(2,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getFeb()))));
                barEntries.add(new BarEntry(3,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getMar()))));
                barEntries.add(new BarEntry(4,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getApr()))));
                barEntries.add(new BarEntry(5,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getMay()))));
                barEntries.add(new BarEntry(6,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getJun()))));
                barEntries.add(new BarEntry(7,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getJul()))));
                barEntries.add(new BarEntry(8,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getAug()))));
                barEntries.add(new BarEntry(9,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getSep()))));
                barEntries.add(new BarEntry(10,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getOct()))));
                barEntries.add(new BarEntry(11,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getNov()))));
                barEntries.add(new BarEntry(12,Float.parseFloat(String.valueOf(sellerStatsSalesList.get(0).getDec()))));

                double[] totalSales = new double[]{sellerStatsSalesList.get(0).getJan(),
                        sellerStatsSalesList.get(0).getFeb(),
                        sellerStatsSalesList.get(0).getMar(),
                        sellerStatsSalesList.get(0).getApr(),
                        sellerStatsSalesList.get(0).getMay(),
                        sellerStatsSalesList.get(0).getJun(),
                        sellerStatsSalesList.get(0).getJul(),
                        sellerStatsSalesList.get(0).getAug(),
                        sellerStatsSalesList.get(0).getSep(),
                        sellerStatsSalesList.get(0).getOct(),
                        sellerStatsSalesList.get(0).getNov(),
                        sellerStatsSalesList.get(0).getDec()};
                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                double finalTotal = 0;
                for(int i =0; i<12; i++)
                {
                    finalTotal = totalSales[i] + finalTotal;
                }
                totalSalesTxt.setText("₱"+String.valueOf(myFormat.format(finalTotal)));
                avgSalesTxt.setText("₱"+String.valueOf(myFormat.format(finalTotal/12))+" - AVG");


                BarDataSet barDataSet = new BarDataSet(barEntries, "Sales");
                barDataSet.setColor(Color.GREEN);
                BarData data = new BarData(barDataSet);
                barchartSales.setData(data);
                data.setBarWidth(0.50f);

                String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                XAxis xAxis = barchartSales.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);

                barchartSales.setDragEnabled(true);

                float barSpace = 0.08f;
                float groupSpace = 0.44f;

                barchartSales.getXAxis().setAxisMinimum(0.5f);
                //barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*12);
                barchartSales.getAxisLeft().setAxisMinimum(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    public void viewShopViews()
    {

        sellerStatsShopViewsList = new ArrayList<>();
        Legend legend = barchartShopViews.getLegend();
        legend.setTextSize(20f);
        barchartShopViews.setDrawBarShadow(false);
        barchartShopViews.setDrawValueAboveBar(true);
        barchartShopViews.setMaxVisibleValueCount(100);
        barchartShopViews.setPinchZoom(true);
        barchartShopViews.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stats").child(user.getUid()).child("2022-ShopViews");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sellerStatsShopViewsList.clear();
                barEntries.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SellerStatsShopViews orders = snapshot.getValue(SellerStatsShopViews.class);
                    sellerStatsShopViewsList.add(orders);

                }
                barEntries.add(new BarEntry(1,sellerStatsShopViewsList.get(0).getJan()));
                barEntries.add(new BarEntry(2,sellerStatsShopViewsList.get(0).getFeb()));
                barEntries.add(new BarEntry(3,sellerStatsShopViewsList.get(0).getMar()));
                barEntries.add(new BarEntry(4,sellerStatsShopViewsList.get(0).getApr()));
                barEntries.add(new BarEntry(5,sellerStatsShopViewsList.get(0).getMay()));
                barEntries.add(new BarEntry(6,sellerStatsShopViewsList.get(0).getJun()));
                barEntries.add(new BarEntry(7,sellerStatsShopViewsList.get(0).getJul()));
                barEntries.add(new BarEntry(8,sellerStatsShopViewsList.get(0).getAug()));
                barEntries.add(new BarEntry(9,sellerStatsShopViewsList.get(0).getSep()));
                barEntries.add(new BarEntry(10,sellerStatsShopViewsList.get(0).getOct()));
                barEntries.add(new BarEntry(11,sellerStatsShopViewsList.get(0).getNov()));
                barEntries.add(new BarEntry(12,sellerStatsShopViewsList.get(0).getDec()));

                int[] totalShopViews = new int[]{sellerStatsShopViewsList.get(0).getJan(),
                        sellerStatsShopViewsList.get(0).getFeb(),
                        sellerStatsShopViewsList.get(0).getMar(),
                        sellerStatsShopViewsList.get(0).getApr(),
                        sellerStatsShopViewsList.get(0).getMay(),
                        sellerStatsShopViewsList.get(0).getJun(),
                        sellerStatsShopViewsList.get(0).getJul(),
                        sellerStatsShopViewsList.get(0).getAug(),
                        sellerStatsShopViewsList.get(0).getSep(),
                        sellerStatsShopViewsList.get(0).getOct(),
                        sellerStatsShopViewsList.get(0).getNov(),
                        sellerStatsShopViewsList.get(0).getDec()};
                NumberFormat myFormat = NumberFormat.getInstance();
                myFormat.setGroupingUsed(true);
                int finalTotal = 0;
                for(int i =0; i<12; i++)
                {
                    finalTotal = totalShopViews[i] + finalTotal;
                }
                totalShopViewsTxt.setText(String.valueOf(myFormat.format(finalTotal)));
                avgShopViewsTxt.setText(String.valueOf(myFormat.format(finalTotal/12))+" - AVG");


                BarDataSet barDataSet = new BarDataSet(barEntries, "Shop Views");
                barDataSet.setColor(Color.BLUE);
                BarData data = new BarData(barDataSet);
                barchartShopViews.setData(data);
                data.setBarWidth(0.50f);

                String[] months = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
                XAxis xAxis = barchartShopViews.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                xAxis.setGranularity(1);
                xAxis.setGranularityEnabled(true);

                barchartShopViews.setDragEnabled(true);

                float barSpace = 0.08f;
                float groupSpace = 0.44f;

                barchartShopViews.getXAxis().setAxisMinimum(0.5f);
                //barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*12);
                barchartShopViews.getAxisLeft().setAxisMinimum(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Subscription").child(user.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    SubscriptionList list = snapshot.getValue(SubscriptionList.class);
                    if(list.getSubscriptionStatus().equalsIgnoreCase("Regular"))
                    {
                        alertMessageCardView.setVisibility(View.VISIBLE);
                        barChartOrders.setVisibility(View.INVISIBLE);
                        barchartSales.setVisibility(View.INVISIBLE);
                        barchartShopViews.setVisibility(View.INVISIBLE);
                        ordersCardview.setVisibility(View.INVISIBLE);
                        salesCardview.setVisibility(View.INVISIBLE);
                        shopViewsCardview.setVisibility(View.INVISIBLE);

                    }
                    else
                    {
                        Toast.makeText(getContext(), "Premium Account", Toast.LENGTH_SHORT).show();
                        alertMessageCardView.setVisibility(View.INVISIBLE);
                        viewOrders();
                        viewSales();
                        viewShopViews();
                        barChartOrders.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}