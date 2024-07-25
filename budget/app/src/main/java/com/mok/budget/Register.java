package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    private EditText rg_ID_edit, rg_pwd_edit, rg_pwd_chk_edit, rg_name_edit, rg_asw_edit;
    private Spinner rg_qt_slt;
    private TextView rg_check_txt;
    private Button rg_create_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rg_ID_edit = findViewById(R.id.rg_ID_edit);
        rg_pwd_edit = findViewById(R.id.rg_pwd_edit);
        rg_pwd_chk_edit = findViewById(R.id.rg_pwd_chk_edit);
        rg_name_edit = findViewById(R.id.rg_name_edit);
        rg_qt_slt = findViewById(R.id.rg_qt_slt);
        rg_asw_edit = findViewById(R.id.rg_asw_edit);
        rg_check_txt = findViewById(R.id.rg_check_txt);
        rg_create_btn = findViewById(R.id.rg_create_btn);

        rg_ID_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rg_check_txt.setText("unavailable");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void onCreate(View view) {
        String id = rg_ID_edit.getText().toString();
        String password = rg_pwd_edit.getText().toString();
        String password_chk = rg_pwd_chk_edit.getText().toString();
        String name = rg_name_edit.getText().toString();
        String question = rg_qt_slt.getSelectedItem().toString();
        String answer = rg_asw_edit.getText().toString();

        if (id.isEmpty() || password.isEmpty() || password_chk.isEmpty() || name.isEmpty() || question.isEmpty() || answer.isEmpty()){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
        }else if (password.length() < 8 || password.length() > 16){
            Toast.makeText(getApplicationContext(), "password length should be 8 ~ 16!!", Toast.LENGTH_SHORT).show();
        }else if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*") || !password.matches(".*[!@#$%^&*()].*")){
            Toast.makeText(getApplicationContext(), "Password must contain Capital letter, Small letter, Numbers, and Special characters.!!", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(password_chk)){
            Toast.makeText(getApplicationContext(), "Different password!!", Toast.LENGTH_SHORT).show();
        }else if (!rg_check_txt.getText().toString().equals("available")){
            Toast.makeText(getApplicationContext(), "check the ID", Toast.LENGTH_SHORT).show();
        }else{
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(getApplicationContext(), "Create Success!!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Create fail!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            RegisterRequest registerRequest = new RegisterRequest(id, password, name, question, answer, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Register.this);
            queue.add(registerRequest);
        }
    }
    public void check_ocb(View view) {
        String id = rg_ID_edit.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        rg_check_txt.setText("unavailable");
                    }else{
                        rg_check_txt.setText("available");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        checkRequest checkRequest = new checkRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(checkRequest);
    }
}