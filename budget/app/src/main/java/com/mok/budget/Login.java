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

public class Login extends AppCompatActivity {

    private TextView ID_edit, pwd_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ID_edit = findViewById(R.id.ID_edit);
        pwd_edit = findViewById(R.id.pwd_edit);

    }
    public void onButtonClick(View view) {

        String id = ID_edit.getText().toString();
        String password = pwd_edit.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        String id = jsonObject.getString("id");
                        String password = jsonObject.getString("password");
                        Toast.makeText(getApplicationContext(), "Welcome!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Main_scn.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplicationContext(), "Login fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        loginRequest loginRequest = new loginRequest(id, password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(loginRequest);
    }

    public void onRegister(View view){
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }

    public void onRecovery(View view){
        Intent intent = new Intent(Login.this, password_recovery1.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        finishAffinity();
        super.onBackPressed();
    }
}