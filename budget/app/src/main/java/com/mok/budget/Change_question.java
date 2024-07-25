package com.mok.budget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Change_question extends AppCompatActivity {

    private String id;
    private Spinner chg_qst_slt;
    private EditText chg_answer_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_question);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        chg_qst_slt = findViewById(R.id.chg_qst_slt);
        chg_answer_edit = findViewById(R.id.chg_answer_edit);
    }

    public void onEdit (View view){
        String question = chg_qst_slt.getSelectedItem().toString();
        String answer = chg_answer_edit.getText().toString();

        if (answer.equals("")){
            Toast.makeText(getApplicationContext(), "Insert all blank!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(getApplicationContext(), "Change Success!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(), "Change fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ChangeqstRequest ChangeqstRequest = new ChangeqstRequest(id, question, answer, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Change_question.this);
        queue.add(ChangeqstRequest);
    }
}