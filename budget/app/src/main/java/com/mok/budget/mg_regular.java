package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class mg_regular extends AppCompatActivity {

    private String id;
    private ListView weekly_list;
    private String[][] week_list;
    private String[] list_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mg_regular);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        weekly_list = findViewById(R.id.weekly_list);

        weekly_load();
    }

    public void Add_click(View view){
        Intent intent = new Intent(mg_regular.this, add_regular.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void weekly_load(){

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

                        week_list = new String[count][4];
                        list_item = new String[count];

                        for (int i = 0; i < count; i++){
                            week_list[i][0] = jsonObject.getString("chose" + (i+1));
                            date = jsonObject.getString("date" + (i+1));
                            Date parsedDate = format.parse(date);
                            week_list[i][1] = format.format(parsedDate);
                            week_list[i][2] = jsonObject.getString("name" + (i+1));
                            week_list[i][3] = "$" + jsonObject.getString("price" + (i+1));

                            if (week_list[i][0].equals("1")){
                                list_item[i] = "Income " + week_list[i][2] + " " + week_list[i][1] + " " + week_list[i][3];

                            }else{
                                list_item[i] = "Expense " + week_list[i][2] + " " + week_list[i][1] + " " + week_list[i][3];
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, list_item);
                        weekly_list.setAdapter(adapter);

                        weekly_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                onclickEdit(position);
                            }
                        });

                    }else{
                        weekly_list.setAdapter(null);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        weeklylistRequest weeklylistRequest = new weeklylistRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(mg_regular.this);
        queue.add(weeklylistRequest);
    }

    public void onclickEdit(int count){
        Intent intent = new Intent(mg_regular.this, edit_regular.class);
        intent.putExtra("id", id);
        intent.putExtra("chose", week_list[count][0]);
        intent.putExtra("date", week_list[count][1]);
        intent.putExtra("name", week_list[count][2]);
        intent.putExtra("price", week_list[count][3]);
        startActivity(intent);
    }

    public void onResume() {//if get finish() reload
        super.onResume();

        weekly_load();
    }
}