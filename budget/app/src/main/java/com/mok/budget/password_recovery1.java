package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class password_recovery1 extends AppCompatActivity {

    private TextView re_ID_edit, re_name_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery1);

        re_ID_edit = findViewById(R.id.re_ID_edit);
        re_name_edit = findViewById(R.id.re_name_edit);
    }

    public void onNext1(View view){

        String id = re_ID_edit.getText().toString();
        String name = re_name_edit.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String id = jsonObject.getString("id");
                        Intent intent = new Intent(password_recovery1.this, password_recovery2.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "The name or ID do not exist!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        pwdRecoveryRequest1 pwdRecoveryRequest1 = new pwdRecoveryRequest1(id, name, responseListener);
        RequestQueue queue = Volley.newRequestQueue(password_recovery1.this);
        queue.add(pwdRecoveryRequest1);
    }

}