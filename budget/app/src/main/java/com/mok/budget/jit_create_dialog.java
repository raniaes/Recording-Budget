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

public class jit_create_dialog extends Dialog {

    private EditText jit_create_edit;
    private Button jit_add_btn, jit_cn_btn;
    private String id;
    private Context context;
    private DialogDismissListener dismissListener;

    public jit_create_dialog(@NonNull Context context, String id, DialogDismissListener dismissListener) {
        super(context);
        setContentView(R.layout.activity_jit_create_dialog);

        this.id = id;
        this.context = context;
        this.dismissListener = dismissListener;

        jit_create_edit = findViewById(R.id.jit_create_edit);
        jit_add_btn = findViewById(R.id.jit_add_btn);
        jit_cn_btn = findViewById(R.id.jit_cn_btn);

        jit_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_click();
            }
        });

        jit_cn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public interface DialogDismissListener {
        void onDialogDismiss();
    }

    public void create_click(){

        String name = jit_create_edit.getText().toString();

        if (jit_create_edit.getText().toString().isEmpty()){
            Toast.makeText(context, "Fill in the blank!!", Toast.LENGTH_SHORT).show();
            return;
        }else{
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success){
                            Toast.makeText(context, "Create Success!!", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }else{
                            Toast.makeText(context, "Create fail!!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            addjointRequest addjointRequest = new addjointRequest(id, name, responseListener);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(addjointRequest);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (dismissListener != null) {
            dismissListener.onDialogDismiss();
        }
    }
}
