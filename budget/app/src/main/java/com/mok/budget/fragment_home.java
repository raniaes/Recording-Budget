package com.mok.budget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class fragment_home extends Fragment {

    private Bundle bundle;
    private String id, startDate, endDate, second_id, admin;
    private TextView hm_tg_name, hm_income, hm_date_tv;
    private Button left_btn, right_btn, hm_add_btn;
    private ListView daily_list;
    private String[][] date_list;
    private String[] list_item;
    private PieChart pie_left, pie_expense, pie_target;
    private Context context;
    private ConstraintLayout constraintLayout4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        bundle = getArguments();
        id = bundle.getString("id");
        second_id = bundle.getString("second_id");

        hm_tg_name = view.findViewById(R.id.hm_tg_name);
        hm_income = view.findViewById(R.id.hm_income);
        hm_date_tv = view.findViewById(R.id.hm_date_tv);
        left_btn = view.findViewById(R.id.left_btn);
        right_btn = view.findViewById(R.id.right_btn);
        hm_add_btn = view.findViewById(R.id.hm_add_btn);
        daily_list = view.findViewById(R.id.daily_list);
        pie_left = view.findViewById(R.id.pie_left);
        pie_expense = view.findViewById(R.id.pie_expense);
        pie_target = view.findViewById(R.id.pie_target);
        constraintLayout4 = view.findViewById(R.id.constraintLayout4);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        startDate = sdf.format(cal.getTime());

        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        endDate = sdf.format(cal.getTime());

        if (second_id != null){
            constraintLayout4.setVisibility(View.GONE);
        }


        //weekly

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String income = jsonObject.getString("sum_true");
                        String left = jsonObject.getString("left");
                        String expense = jsonObject.getString("sum_false");
                        hm_income.setText("Weekly Income : " + income);

                        //remaining money grape
                        List<PieEntry> ch_income = new ArrayList<>();
                        ch_income.add(new PieEntry(Float.parseFloat(left),""));
                        ch_income.add(new PieEntry(Integer.parseInt(income)-Integer.parseInt(left),""));

                        PieDataSet set_income = new PieDataSet(ch_income,"Remaining money");
                        set_income.setColors(Color.BLUE,Color.TRANSPARENT);
                        set_income.setDrawValues(false);

                        PieData data_income = new PieData(set_income);

                        pie_left.setCenterText("Remaining\n$ " + left);
                        pie_left.setCenterTextColor(Color.BLUE);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_left.setCenterTextTypeface(boldTypeface);

                        pie_left.getDescription().setEnabled(false);
                        pie_left.getLegend().setEnabled(false);
                        pie_left.setData(data_income);
                        pie_left.animateY(2000, Easing.EaseInOutQuad);
                        pie_left.invalidate();

                        //expense grape
                        List<PieEntry> ch_expense = new ArrayList<>();
                        ch_expense.add(new PieEntry(Float.parseFloat(expense),""));
                        if (Integer.parseInt(income) >= Integer.parseInt(expense)){
                            ch_expense.add(new PieEntry(Integer.parseInt(income)-Integer.parseInt(expense),""));
                        }else {
                            ch_expense.add(new PieEntry(0,""));
                        }

                        PieDataSet set_expense = new PieDataSet(ch_expense,"Expense");
                        set_expense.setColors(Color.RED,Color.TRANSPARENT);
                        set_expense.setDrawValues(false);

                        PieData data_expense = new PieData(set_expense);

                        pie_expense.setCenterText("Expense\n$ " + expense);
                        pie_expense.setCenterTextColor(Color.RED);
                        pie_expense.setCenterTextTypeface(boldTypeface);

                        pie_expense.getDescription().setEnabled(false);
                        pie_expense.getLegend().setEnabled(false);
                        pie_expense.setData(data_expense);
                        pie_expense.animateY(2000, Easing.EaseInOutQuad);
                        pie_expense.invalidate();

                    }else{
                        hm_income.setText("Weekly Income : " + 0);

                        //remaining money grape
                        List<PieEntry> ch_income = new ArrayList<>();
                        ch_income.add(new PieEntry(0,""));
                        ch_income.add(new PieEntry(0,""));

                        PieDataSet set_income = new PieDataSet(ch_income,"Remaining money");
                        set_income.setColors(Color.BLUE,Color.TRANSPARENT);
                        set_income.setDrawValues(false);

                        PieData data_income = new PieData(set_income);

                        pie_left.setCenterText("Remaining\n$ " + 0);
                        pie_left.setCenterTextColor(Color.BLUE);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_left.setCenterTextTypeface(boldTypeface);

                        pie_left.getDescription().setEnabled(false);
                        pie_left.getLegend().setEnabled(false);
                        pie_left.setData(data_income);
                        pie_left.animateY(2000, Easing.EaseInOutQuad);
                        pie_left.invalidate();

                        //expense grape
                        List<PieEntry> ch_expense = new ArrayList<>();
                        ch_expense.add(new PieEntry(0,""));
                        ch_expense.add(new PieEntry(0,""));

                        PieDataSet set_expense = new PieDataSet(ch_expense,"Remaining money");
                        set_expense.setColors(Color.RED,Color.TRANSPARENT);
                        set_expense.setDrawValues(false);

                        PieData data_expense = new PieData(set_expense);

                        pie_expense.setCenterText("Expense\n$ " + 0);
                        pie_expense.setCenterTextColor(Color.RED);
                        pie_expense.setCenterTextTypeface(boldTypeface);

                        pie_expense.getDescription().setEnabled(false);
                        pie_expense.getLegend().setEnabled(false);
                        pie_expense.setData(data_expense);
                        pie_expense.animateY(2000, Easing.EaseInOutQuad);
                        pie_expense.invalidate();

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        weeklyviewRequest weeklyviewRequest = new weeklyviewRequest(id, startDate, endDate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(weeklyviewRequest);


        //Target

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    String target = jsonObject.getString("target");
                    String price = jsonObject.getString("price");
                    String left = jsonObject.getString("left");

                    if (target.equals("null")){
                        hm_tg_name.setText("Target" + " : " + 0);

                        List<PieEntry> ch_target = new ArrayList<>();
                        ch_target.add(new PieEntry(0,""));
                        ch_target.add(new PieEntry(0,""));

                        PieDataSet set_target = new PieDataSet(ch_target,"Remaining money");
                        set_target.setColors(Color.parseColor("#8BC34A"),Color.TRANSPARENT);
                        set_target.setDrawValues(false);

                        PieData data_target = new PieData(set_target);

                        pie_target.setCenterText(0 + " %");
                        pie_target.setCenterTextColor(Color.parseColor("#8BC34A"));
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_target.setCenterTextTypeface(boldTypeface);

                        pie_target.getDescription().setEnabled(false);
                        pie_target.getLegend().setEnabled(false);
                        pie_target.setData(data_target);
                        pie_target.animateY(2000, Easing.EaseInOutQuad);
                        pie_target.invalidate();
                    }else{
                        hm_tg_name.setText(target + " : " + price);

                        List<PieEntry> ch_target = new ArrayList<>();
                        ch_target.add(new PieEntry(Float.parseFloat(left),""));
                        ch_target.add(new PieEntry(100 - Float.parseFloat(left),""));

                        PieDataSet set_target = new PieDataSet(ch_target,"Remaining money");
                        set_target.setColors(Color.parseColor("#8BC34A"),Color.TRANSPARENT);
                        set_target.setDrawValues(false);

                        PieData data_target = new PieData(set_target);

                        pie_target.setCenterText(left + " %");
                        pie_target.setCenterTextColor(Color.parseColor("#8BC34A"));
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_target.setCenterTextTypeface(boldTypeface);

                        pie_target.getDescription().setEnabled(false);
                        pie_target.getLegend().setEnabled(false);
                        pie_target.setData(data_target);
                        pie_target.animateY(2000, Easing.EaseInOutQuad);
                        pie_target.invalidate();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        tgviewRequest tgviewRequest = new tgviewRequest(id, responseListener);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(tgviewRequest);


        //daily

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = sdf.format(now);


        hm_date_tv.setText(todayString);


        //load date list from mysql
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        int count = jsonObject.getInt("count");
                        String date;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        Date datetime;

                        date_list = new String[count][4];
                        list_item = new String[count];

                        for (int i = 0; i < count; i++){
                            date_list[i][0] = jsonObject.getString("chose" + (i+1));
                            date = jsonObject.getString("date" + (i+1));
                            datetime = format.parse(date);
                            date_list[i][1] = timeFormat.format(datetime);
                            date_list[i][2] = jsonObject.getString("name" + (i+1));
                            date_list[i][3] = "$" + jsonObject.getString("price" + (i+1));

                            if (date_list[i][0].equals("1")){
                                list_item[i] = "Income " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                            }else{
                                list_item[i] = "Expense " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list_item);
                        daily_list.setAdapter(adapter);

                        daily_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                onclickEdit(position);
                            }
                        });

                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        dailylistRequest dailylistRequest = new dailylistRequest(id, hm_date_tv.getText().toString(), responseListener);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(dailylistRequest);



        //left button
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    left_btn_click();
                    daily_list.setAdapter(null);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    int count = jsonObject.getInt("count");
                                    String date;
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    Date datetime;

                                    date_list = new String[count][4];
                                    list_item = new String[count];

                                    for (int i = 0; i < count; i++){
                                        date_list[i][0] = jsonObject.getString("chose" + (i+1));
                                        date = jsonObject.getString("date" + (i+1));
                                        datetime = format.parse(date);
                                        date_list[i][1] = timeFormat.format(datetime);
                                        date_list[i][2] = jsonObject.getString("name" + (i+1));
                                        date_list[i][3] = "$" + jsonObject.getString("price" + (i+1));

                                        if (date_list[i][0].equals("1")){
                                            list_item[i] = "Income " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                                        }else{
                                            list_item[i] = "Expense " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list_item);
                                    daily_list.setAdapter(adapter);

                                    daily_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            onclickEdit(position);
                                        }
                                    });

                                }else{
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    dailylistRequest dailylistRequest = new dailylistRequest(id, hm_date_tv.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(dailylistRequest);

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        //right button
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    right_btn_click();
                    daily_list.setAdapter(null);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success){
                                    int count = jsonObject.getInt("count");
                                    String date;
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                                    Date datetime;

                                    date_list = new String[count][4];
                                    list_item = new String[count];

                                    for (int i = 0; i < count; i++){
                                        date_list[i][0] = jsonObject.getString("chose" + (i+1));
                                        date = jsonObject.getString("date" + (i+1));
                                        datetime = format.parse(date);
                                        date_list[i][1] = timeFormat.format(datetime);
                                        date_list[i][2] = jsonObject.getString("name" + (i+1));
                                        date_list[i][3] = "$" + jsonObject.getString("price" + (i+1));

                                        if (date_list[i][0].equals("1")){
                                            list_item[i] = "Income " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                                        }else{
                                            list_item[i] = "Expense " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                                        }
                                    }

                                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list_item);
                                    daily_list.setAdapter(adapter);

                                    daily_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                            onclickEdit(position);
                                        }
                                    });

                                }else{
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };

                    dailylistRequest dailylistRequest = new dailylistRequest(id, hm_date_tv.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    queue.add(dailylistRequest);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        hm_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickAdd();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void left_btn_click() throws ParseException {
        String left = hm_date_tv.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(left);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, -1);

        date = calendar.getTime();

        hm_date_tv.setText(format.format(date));
    }

    public void right_btn_click() throws ParseException {
        String right = hm_date_tv.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(right);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, 1);

        date = calendar.getTime();

        hm_date_tv.setText(format.format(date));
    }

    public void onclickAdd() {
        Intent intent = new Intent(getActivity(), add_daily.class);
        intent.putExtra("id", id);
        intent.putExtra("date", hm_date_tv.getText().toString());
        startActivity(intent);
    }

    public void onclickEdit(int count){
        Intent intent = new Intent(getActivity(), edit_daily.class);
        intent.putExtra("id", id);
        intent.putExtra("chose", date_list[count][0]);
        intent.putExtra("date", hm_date_tv.getText().toString() + " " + date_list[count][1]);
        intent.putExtra("name", date_list[count][2]);
        intent.putExtra("price", date_list[count][3]);
        startActivity(intent);
    }

    @Override
    public void onResume() {//if get finish() reload
        super.onResume();

        //weekly reload
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String income = jsonObject.getString("sum_true");
                        String left = jsonObject.getString("left");
                        String expense = jsonObject.getString("sum_false");
                        hm_income.setText("Weekly Income : " + income);

                        //Remaining money grape
                        List<PieEntry> ch_income = new ArrayList<>();
                        ch_income.add(new PieEntry(Float.parseFloat(left),""));
                        ch_income.add(new PieEntry(Integer.parseInt(income)-Integer.parseInt(left),""));

                        PieDataSet set_income = new PieDataSet(ch_income,"Remaining money");
                        set_income.setColors(Color.BLUE,Color.TRANSPARENT);
                        set_income.setDrawValues(false);

                        PieData data_income = new PieData(set_income);

                        pie_left.setCenterText("Remaining\n$ " + left);
                        pie_left.setCenterTextColor(Color.BLUE);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_left.setCenterTextTypeface(boldTypeface);

                        pie_left.getDescription().setEnabled(false);
                        pie_left.getLegend().setEnabled(false);
                        pie_left.setData(data_income);
                        pie_left.animateY(2000, Easing.EaseInOutQuad);
                        pie_left.invalidate();

                        //expense grape
                        List<PieEntry> ch_expense = new ArrayList<>();
                        ch_expense.add(new PieEntry(Float.parseFloat(expense),""));
                        if (Integer.parseInt(income) >= Integer.parseInt(expense)){
                            ch_expense.add(new PieEntry(Integer.parseInt(income)-Integer.parseInt(expense),""));
                        }else {
                            ch_expense.add(new PieEntry(0,""));
                        }

                        PieDataSet set_expense = new PieDataSet(ch_expense,"Expense");
                        set_expense.setColors(Color.RED,Color.TRANSPARENT);
                        set_expense.setDrawValues(false);

                        PieData data_expense = new PieData(set_expense);

                        pie_expense.setCenterText("Expense\n$ " + expense);
                        pie_expense.setCenterTextColor(Color.RED);
                        pie_expense.setCenterTextTypeface(boldTypeface);

                        pie_expense.getDescription().setEnabled(false);
                        pie_expense.getLegend().setEnabled(false);
                        pie_expense.setData(data_expense);
                        pie_expense.animateY(2000, Easing.EaseInOutQuad);
                        pie_expense.invalidate();

                    }else{
                        hm_income.setText("Weekly Income : " + 0);

                        //Remaining money grape
                        List<PieEntry> ch_income = new ArrayList<>();
                        ch_income.add(new PieEntry(0,""));
                        ch_income.add(new PieEntry(0,""));

                        PieDataSet set_income = new PieDataSet(ch_income,"Remaining money");
                        set_income.setColors(Color.BLUE,Color.TRANSPARENT);
                        set_income.setDrawValues(false);

                        PieData data_income = new PieData(set_income);

                        pie_left.setCenterText("Remaining\n$ " + 0);
                        pie_left.setCenterTextColor(Color.BLUE);
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_left.setCenterTextTypeface(boldTypeface);

                        pie_left.getDescription().setEnabled(false);
                        pie_left.getLegend().setEnabled(false);
                        pie_left.setData(data_income);
                        pie_left.animateY(2000, Easing.EaseInOutQuad);
                        pie_left.invalidate();

                        //expense grape
                        List<PieEntry> ch_expense = new ArrayList<>();
                        ch_expense.add(new PieEntry(0,""));
                        ch_expense.add(new PieEntry(0,""));

                        PieDataSet set_expense = new PieDataSet(ch_expense,"expense");
                        set_expense.setColors(Color.RED,Color.TRANSPARENT);
                        set_expense.setDrawValues(false);

                        PieData data_expense = new PieData(set_expense);

                        pie_expense.setCenterText("Expense\n$ " + 0);
                        pie_expense.setCenterTextColor(Color.RED);
                        pie_expense.setCenterTextTypeface(boldTypeface);

                        pie_expense.getDescription().setEnabled(false);
                        pie_expense.getLegend().setEnabled(false);
                        pie_expense.setData(data_expense);
                        pie_expense.animateY(2000, Easing.EaseInOutQuad);
                        pie_expense.invalidate();

                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        weeklyviewRequest weeklyviewRequest = new weeklyviewRequest(id, startDate, endDate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(weeklyviewRequest);

        //target reload
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    String target = jsonObject.getString("target");
                    String price = jsonObject.getString("price");
                    String left = jsonObject.getString("left");

                    if (target.equals("null")){
                        hm_tg_name.setText("Target" + " : " + 0);

                        List<PieEntry> ch_target = new ArrayList<>();
                        ch_target.add(new PieEntry(0,""));
                        ch_target.add(new PieEntry(0,""));

                        PieDataSet set_target = new PieDataSet(ch_target,"Remaining money");
                        set_target.setColors(Color.parseColor("#8BC34A"),Color.TRANSPARENT);
                        set_target.setDrawValues(false);

                        PieData data_target = new PieData(set_target);

                        pie_target.setCenterText(0 + " %");
                        pie_target.setCenterTextColor(Color.parseColor("#8BC34A"));
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_target.setCenterTextTypeface(boldTypeface);

                        pie_target.getDescription().setEnabled(false);
                        pie_target.getLegend().setEnabled(false);
                        pie_target.setData(data_target);
                        pie_target.animateY(2000, Easing.EaseInOutQuad);
                        pie_target.invalidate();
                    }else{
                        hm_tg_name.setText(target + " : " + price);

                        List<PieEntry> ch_target = new ArrayList<>();
                        ch_target.add(new PieEntry(Float.parseFloat(left),""));
                        ch_target.add(new PieEntry(100 - Float.parseFloat(left),""));

                        PieDataSet set_target = new PieDataSet(ch_target,"Remaining money");
                        set_target.setColors(Color.parseColor("#8BC34A"),Color.TRANSPARENT);
                        set_target.setDrawValues(false);

                        PieData data_target = new PieData(set_target);

                        pie_target.setCenterText(left + " %");
                        pie_target.setCenterTextColor(Color.parseColor("#8BC34A"));
                        Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
                        pie_target.setCenterTextTypeface(boldTypeface);

                        pie_target.getDescription().setEnabled(false);
                        pie_target.getLegend().setEnabled(false);
                        pie_target.setData(data_target);
                        pie_target.animateY(2000, Easing.EaseInOutQuad);
                        pie_target.invalidate();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        tgviewRequest tgviewRequest = new tgviewRequest(id, responseListener);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(tgviewRequest);

        //daily reload

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        int count = jsonObject.getInt("count");
                        String date;
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        Date datetime;

                        date_list = new String[count][4];
                        list_item = new String[count];

                        for (int i = 0; i < count; i++) {
                            date_list[i][0] = jsonObject.getString("chose" + (i + 1));
                            date = jsonObject.getString("date" + (i + 1));
                            datetime = format.parse(date);
                            date_list[i][1] = timeFormat.format(datetime);
                            date_list[i][2] = jsonObject.getString("name" + (i + 1));
                            date_list[i][3] = "$" + jsonObject.getString("price" + (i + 1));

                            if (date_list[i][0].equals("1")) {
                                list_item[i] = "Income " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                            } else {
                                list_item[i] = "Expense " + date_list[i][1] + " " + date_list[i][2] + " " + date_list[i][3];
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list_item);
                        daily_list.setAdapter(adapter);

                        daily_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                onclickEdit(position);
                            }
                        });

                    } else {
                        daily_list.setAdapter(null);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        dailylistRequest dailylistRequest = new dailylistRequest(id, hm_date_tv.getText().toString(), responseListener);
        queue = Volley.newRequestQueue(getActivity());
        queue.add(dailylistRequest);
    }
}