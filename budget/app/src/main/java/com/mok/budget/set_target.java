package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class set_target extends AppCompatActivity {

    private String id;
    private EditText tg_name_edit, tg_price_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_target);

        tg_name_edit = findViewById(R.id.tg_name_edit);
        tg_price_edit = findViewById(R.id.tg_price_edit);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

    }

    public void ontgsubmit (View view){
        String target = tg_name_edit.getText().toString();

        if (tg_price_edit.getText().toString().equals("") || target.equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price = Integer.parseInt(tg_price_edit.getText().toString());

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String datetime = sdf.format(now);

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

        changetgRequest changetgRequest = new changetgRequest(id, target, price, datetime, responseListener);
        RequestQueue queue = Volley.newRequestQueue(set_target.this);
        queue.add(changetgRequest);
    }
}