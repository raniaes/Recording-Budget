package com.mok.budget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class jit_adduser_dialog extends Dialog {

    private EditText jit_adduser_edit;
    private Button jit_add_user_btn, jit_cn_user_btn;
    private String code, name;
    private Context context;

    public jit_adduser_dialog(@NonNull Context context, String code, String name) {
        super(context);
        setContentView(R.layout.activity_jit_adduser_dialog);

        this.code = code;
        this.name = name;
        this.context = context;

        jit_adduser_edit = findViewById(R.id.jit_adduser_edit);
        jit_add_user_btn = findViewById(R.id.jit_add_user_btn);
        jit_cn_user_btn = findViewById(R.id.jit_cn_user_btn);

        jit_cn_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        jit_add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclick_adduser();
            }
        });

    }

    public void onclick_adduser(){

        String id = jit_adduser_edit.getText().toString();

        if (jit_adduser_edit.getText().toString().isEmpty()){
            Toast.makeText(context, "Fill in the blank!!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        if (success.equals("2")){
                            Toast.makeText(context, "Add Success!!", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else if (success.equals("1")){
                            Toast.makeText(context, "Already User!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, "User does not exist!!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            adduserjointRequest adduserjointRequest = new adduserjointRequest(id, name, code, responseListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(adduserjointRequest);
        }
    }
}
