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

public class password_recovery3 extends AppCompatActivity {

    private String id;
    private int key;
    private EditText re_pwd_edit, re_pwdck_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_recovery3);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        key = intent.getIntExtra("key",0);

        re_pwd_edit = findViewById(R.id.re_pwd_edit);
        re_pwdck_edit = findViewById(R.id.re_pwdck_edit);
    }

    public void onSubmit(View view){

        String password = re_pwd_edit.getText().toString();

        if(re_pwd_edit.getText().toString().equals("") || re_pwdck_edit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }else if (!re_pwd_edit.getText().toString().equals(re_pwdck_edit.getText().toString())){
            Toast.makeText(getApplicationContext(), "passwords are different!!", Toast.LENGTH_SHORT).show();
            return;
        }else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()].*")) {
            Toast.makeText(getApplicationContext(), "Password must contain Capital letter, Small letter, Numbers, and Special characters.!!", Toast.LENGTH_SHORT).show();
        }else{
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(getApplicationContext(), "reset Success!!", Toast.LENGTH_SHORT).show();
                            if (key == 1){
                                Intent intent = new Intent(password_recovery3.this, Login.class);
                                startActivity(intent);
                            }else{
                                finish();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "reset fail!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            ResetpwdRequest ResetpwdRequest = new ResetpwdRequest(id, password, responseListener);
            RequestQueue queue = Volley.newRequestQueue(password_recovery3.this);
            queue.add(ResetpwdRequest);
        }
    }
}