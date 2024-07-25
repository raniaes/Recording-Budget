package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class edit_regular extends AppCompatActivity {

    private String id, date, name, price, chose;
    private TextView edit_wk_name_edit, edit_wk_price_edit, edit_wk_date_edit, edit_wk_time_edit;
    private ToggleButton edit_wk_in_ex_btn;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_regular);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        chose = intent.getStringExtra("chose");
        date = intent.getStringExtra("date");
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");

        edit_wk_name_edit = findViewById(R.id.edit_wk_name_edit);
        edit_wk_price_edit = findViewById(R.id.edit_wk_price_edit);
        edit_wk_date_edit = findViewById(R.id.edit_wk_date_edit);
        edit_wk_time_edit = findViewById(R.id.edit_wk_time_edit);
        edit_wk_in_ex_btn = findViewById(R.id.edit_wk_in_ex_btn);

        calendar = Calendar.getInstance();

        edit_wk_name_edit.setText(name);
        price = price.replace("$","");
        edit_wk_price_edit.setText(price);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
        Date datetime;

        try {
            datetime = format.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        edit_wk_date_edit.setText(date_format.format(datetime));
        edit_wk_time_edit.setText(time_format.format(datetime));

        if (chose.equals("1")){
            edit_wk_in_ex_btn.setChecked(true);
        }else{
            edit_wk_in_ex_btn.setChecked(false);
        }

        edit_wk_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        edit_wk_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        edit_wk_in_ex_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    chose = "1";
                }else{
                    chose = "0";
                }
            }
        });

    }

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                edit_wk_time_edit.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = sdf.format(calendar.getTime());
                edit_wk_date_edit.setText(formattedDate);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void del_wk_click (View view){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        delweeklyRequest delweeklyRequest = new delweeklyRequest(id, date, name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(edit_regular.this);
        queue.add(delweeklyRequest);
    }

    public void edit_wk_click (View view){
        String re_name = edit_wk_name_edit.getText().toString();
        String re_date = edit_wk_date_edit.getText().toString() + " " + edit_wk_time_edit.getText().toString();
        price = edit_wk_price_edit.getText().toString();


        if (edit_wk_name_edit.getText().toString().equals("") || edit_wk_date_edit.getText().toString().equals("") || edit_wk_price_edit.getText().toString().equals("") || edit_wk_time_edit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        editweeklyRequest editweeklyRequest = new editweeklyRequest(id, date, re_date, name, re_name, Integer.parseInt(chose) , Integer.parseInt(price), responseListener);
        RequestQueue queue = Volley.newRequestQueue(edit_regular.this);
        queue.add(editweeklyRequest);
    }
}