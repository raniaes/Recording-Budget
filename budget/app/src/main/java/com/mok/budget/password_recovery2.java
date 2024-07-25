package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class password_recovery2 extends AppCompatActivity {

    private TextView re_qs_tv;
    private EditText re_answer_edit;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery2);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        re_answer_edit = findViewById(R.id.re_answer_edit);
        re_qs_tv = findViewById(R.id.re_qs_tv);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String question = jsonObject.getString("question");
                    re_qs_tv.setText(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        checkRequest checkRequest = new checkRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(password_recovery2.this);
        queue.add(checkRequest);
    }

    public void onNext2(View view){

        String answer_edt = re_answer_edit.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String id = jsonObject.getString("id");
                    String answer = jsonObject.getString("answer");
                    if (answer_edt.equals(answer)){
                        Intent intent = new Intent(password_recovery2.this, password_recovery3.class);
                        intent.putExtra("id", id);
                        intent.putExtra("key", 1);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Wrong answer", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        checkRequest checkRequest = new checkRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(password_recovery2.this);
        queue.add(checkRequest);



    }
}