package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class daily_list extends AppCompatActivity {

    private String id, date;
    private TextView dl_date;
    private GridView dl_gv;
    private String[][] date_list;
    private GridViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_list);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        date = intent.getStringExtra("date");

        dl_date = findViewById(R.id.dl_date);
        dl_gv = findViewById(R.id.dl_gv);

        dl_date.setText(date);

    }

    public void dl_add_btn_click (View view){
        Intent intent = new Intent(daily_list.this, add_daily.class);
        intent.putExtra("id", id);
        intent.putExtra("date", dl_date.getText().toString());
        startActivity(intent);
    }

    public void dl_left_btn_click (View view) throws ParseException {
        String left = dl_date.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(left);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, -1);

        date = calendar.getTime();

        dl_date.setText(format.format(date));

        dl_gridview();
    }

    public void dl_right_btn_click (View view) throws ParseException {
        String right = dl_date.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(right);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DATE, 1);

        date = calendar.getTime();

        dl_date.setText(format.format(date));

        dl_gridview();
    }

    public void dl_gridview (){

        adapter = new GridViewAdapter(id, dl_date.getText().toString());

        //load date list from mysql
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

                        date_list = new String[count][5];

                        for (int i = 0; i < count; i++){
                            date_list[i][0] = jsonObject.getString("chose" + (i+1));
                            date = jsonObject.getString("date" + (i+1));
                            datetime = format.parse(date);
                            date_list[i][1] = timeFormat.format(datetime);
                            date_list[i][2] = jsonObject.getString("name" + (i+1));
                            date_list[i][3] = jsonObject.getString("price" + (i+1));
                            date_list[i][4] = jsonObject.getString("picture" + (i+1));

                            if (date_list[i][0].equals("1")){
                                adapter.addItem(new grid_item("Income", date_list[i][2], "$" + date_list[i][3], date_list[i][1], date_list[i][4]));
                            }else{
                                adapter.addItem(new grid_item("Expense", date_list[i][2], "$" + date_list[i][3], date_list[i][1], date_list[i][4]));
                            }
                        }

                        dl_gv.setAdapter(adapter);

                    }else{
                        dl_gv.setAdapter(null);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        dailylistRequest dailylistRequest = new dailylistRequest(id, dl_date.getText().toString(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(daily_list.this);
        queue.add(dailylistRequest);
    }

    @Override
    public void onResume() {//if get finish() reload
        super.onResume();

        dl_gridview();
    }

}