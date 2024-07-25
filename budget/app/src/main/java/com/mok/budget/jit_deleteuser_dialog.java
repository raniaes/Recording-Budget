package com.mok.budget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class jit_deleteuser_dialog extends Dialog {

    private Spinner jit_del_user_spinner;
    private Button jit_del_user_btn, jit_cn_del_btn;
    private String code;
    private Context context;
    private String[] joint_user_list;

    public jit_deleteuser_dialog(@NonNull Context context, String code) {
        super(context);
        setContentView(R.layout.activity_jit_deleteuser_dialog);

        this.code = code;
        this.context = context;

        jit_del_user_spinner = findViewById(R.id.jit_del_user_spinner);
        jit_del_user_btn = findViewById(R.id.jit_del_user_btn);
        jit_cn_del_btn = findViewById(R.id.jit_cn_del_btn);

        set_spinner_list();

        jit_del_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick_deluser();
            }
        });

        jit_cn_del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void onclick_deluser(){
        String id = jit_del_user_spinner.getSelectedItem().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(context, "Success!!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }else{
                        Toast.makeText(context, "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        deletejointuserRequest deletejointuserRequest = new deletejointuserRequest(id, code, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deletejointuserRequest);
    }

    public void set_spinner_list(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        int count = jsonObject.getInt("count");

                        joint_user_list = new String[count];

                        for (int i = 0; i < count; i++){
                            joint_user_list[i] = jsonObject.getString("id" + (i+1));
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, joint_user_list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        jit_del_user_spinner.setAdapter(adapter);

                    }else{
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        jointuserlistRequest jointuserlistRequest = new jointuserlistRequest(code, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jointuserlistRequest);
    }
}
