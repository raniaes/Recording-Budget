package com.mok.budget;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class fragment_calendar extends Fragment {

    private Bundle bundle;
    private String id;
    private TextView cal_ts_ed, cal_date_tv;
    private BarChart monthly_chart;
    private Button cal_left_btn, cal_right_btn;
    private CalendarView cal_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        bundle = getArguments();
        id = bundle.getString("id");

        cal_ts_ed = view.findViewById(R.id.cal_ts_ed);
        monthly_chart = view.findViewById(R.id.monthly_chart);
        cal_date_tv = view.findViewById(R.id.cal_date_tv);
        cal_left_btn = view.findViewById(R.id.cal_left_btn);
        cal_right_btn = view.findViewById(R.id.cal_right_btn);
        cal_view = view.findViewById(R.id.cal_view);


        //total save
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String total = jsonObject.getString("total");
                        cal_ts_ed.setText("$ " + total);
                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        totalRequest totalRequest = new totalRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(totalRequest);

        //monthly chart
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        cal_date_tv.setText(String.valueOf(year));

        monthly_list();

        //left btn click
        cal_left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(cal_date_tv.getText().toString());
                year--;
                cal_date_tv.setText(String.valueOf(year));
                monthly_list();
            }
        });

        //right btn click
        cal_right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = Integer.parseInt(cal_date_tv.getText().toString());
                year++;
                cal_date_tv.setText(String.valueOf(year));
                monthly_list();
            }
        });

        //calender view

        cal_view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(calendar.getTime());

                daily_list(formattedDate);
            }
        });

        return view;
    }

    public void monthly_list(){

        String s_year = cal_date_tv.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    String jan = jsonObject.getString("jan");
                    String feb = jsonObject.getString("feb");
                    String mar = jsonObject.getString("mar");
                    String apr = jsonObject.getString("apr");
                    String may = jsonObject.getString("may");
                    String jun = jsonObject.getString("jun");
                    String jul = jsonObject.getString("jul");
                    String aug = jsonObject.getString("aug");
                    String sept = jsonObject.getString("sept");
                    String oct = jsonObject.getString("oct");
                    String nov = jsonObject.getString("nov");
                    String dec = jsonObject.getString("dec");

                    ArrayList<BarEntry> entries = new ArrayList<>();
                    entries.add(new BarEntry(0,Integer.parseInt(jan)));
                    entries.add(new BarEntry(1,Integer.parseInt(feb)));
                    entries.add(new BarEntry(2,Integer.parseInt(mar)));
                    entries.add(new BarEntry(3,Integer.parseInt(apr)));
                    entries.add(new BarEntry(4,Integer.parseInt(may)));
                    entries.add(new BarEntry(5,Integer.parseInt(jun)));
                    entries.add(new BarEntry(6,Integer.parseInt(jul)));
                    entries.add(new BarEntry(7,Integer.parseInt(aug)));
                    entries.add(new BarEntry(8,Integer.parseInt(sept)));
                    entries.add(new BarEntry(9,Integer.parseInt(oct)));
                    entries.add(new BarEntry(10,Integer.parseInt(nov)));
                    entries.add(new BarEntry(11,Integer.parseInt(dec)));


                    BarDataSet dataSet = new BarDataSet(entries, "Monthly");
                    dataSet.setColors(Color.GREEN);
                    dataSet.setValueTextColor(Color.BLACK);
                    dataSet.setValueTextSize(10f);

                    BarData barData = new BarData(dataSet);

                    monthly_chart.setData(barData);
                    monthly_chart.setFitBars(true);

                    XAxis xAxis = monthly_chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setGranularity(1f);

                    String[] labels = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

                    YAxis leftAxis = monthly_chart.getAxisLeft();
                    leftAxis.setDrawGridLines(false);

                    YAxis rightAxis = monthly_chart.getAxisRight();
                    rightAxis.setEnabled(false);

                    monthly_chart.getDescription().setEnabled(false);
                    monthly_chart.getLegend().setEnabled(false);
                    monthly_chart.animateY(1000);

                    monthly_chart.invalidate();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MonthlyRequest MonthlyRequest = new MonthlyRequest(id, s_year, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(MonthlyRequest);

    }

    public void daily_list(String date){
        Intent intent = new Intent(getActivity(),daily_list.class);
        intent.putExtra("id", id);
        intent.putExtra("date", date);

        startActivity(intent);
    }

    public void onResume() {//if get finish() reload
        super.onResume();

        //total save
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String total = jsonObject.getString("total");
                        cal_ts_ed.setText("$ " + total);
                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        totalRequest totalRequest = new totalRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(totalRequest);

        //monthly chart reload
        monthly_list();

    }
}