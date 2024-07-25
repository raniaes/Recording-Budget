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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_regular extends AppCompatActivity {

    private String id;
    boolean chose;
    private ToggleButton in_wk_ex_btn;
    private TextView add_wk_date_edit, add_wk_time_edit, add_wk_name_edit, add_wk_price_edit;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_regular);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        in_wk_ex_btn = findViewById(R.id.in_wk_ex_btn);
        add_wk_date_edit = findViewById(R.id.add_wk_date_edit);
        add_wk_time_edit = findViewById(R.id.add_wk_time_edit);
        add_wk_name_edit = findViewById(R.id.add_wk_name_edit);
        add_wk_price_edit = findViewById(R.id.add_wk_price_edit);

        chose = false;
        in_wk_ex_btn.setChecked(chose);

        calendar = Calendar.getInstance();

        add_wk_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        add_wk_time_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        in_wk_ex_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chose = isChecked;
            }
        });
    }

    private void showTimePickerDialog() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                add_wk_time_edit.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
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
                add_wk_date_edit.setText(formattedDate);
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void add_wk_click(View view){
        if (add_wk_name_edit.getText().toString().equals("") || add_wk_price_edit.getText().toString().equals("") || add_wk_date_edit.getText().toString().equals("") || add_wk_time_edit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Calendar calendar = Calendar.getInstance();
            int second = calendar.get(Calendar.SECOND);
            String date = add_wk_date_edit.getText().toString() + " " + add_wk_time_edit.getText().toString() + ":" + String.format(Locale.getDefault(), "%02d", second);//sdf.format(now);
            String name = add_wk_name_edit.getText().toString();
            int price = Integer.parseInt(add_wk_price_edit.getText().toString());

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(getApplicationContext(), "Success!!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "fail!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            addweeklyRequest addweeklyRequest = new addweeklyRequest(id, date, name, chose, price, responseListener);
            RequestQueue queue = Volley.newRequestQueue(add_regular.this);
            queue.add(addweeklyRequest);
        }
    }
}