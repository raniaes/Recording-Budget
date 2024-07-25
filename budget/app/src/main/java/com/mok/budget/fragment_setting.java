package com.mok.budget;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class fragment_setting extends Fragment {

    private Bundle bundle;
    private String id, password;
    private Button s_chg_pwd_btn, s_chg_qst_btn, s_target_btn, s_regular_btn, s_lgout_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        s_chg_pwd_btn = view.findViewById(R.id.s_chg_pwd_btn);
        s_chg_qst_btn = view.findViewById(R.id.s_chg_qst_btn);
        s_target_btn = view.findViewById(R.id.s_target_btn);
        s_regular_btn = view.findViewById(R.id.s_regular_btn);
        s_lgout_btn = view.findViewById(R.id.s_lgout_btn);

        bundle = getArguments();
        id = bundle.getString("id");

        s_chg_pwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_change_pwd();
            }
        });

        s_chg_qst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_change_qst();
            }
        });

        s_target_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_target();
            }
        });

        s_regular_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_regular();
            }
        });

        s_lgout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_out();
            }
        });

        return view;
    }

    public void set_change_pwd() {
        Intent intent = new Intent(getActivity(), password_recovery3.class);
        intent.putExtra("id", id);
        intent.putExtra("key", 0);
        startActivity(intent);
    }

    public void set_change_qst(){
        Intent intent = new Intent(getActivity(), Change_question.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void set_target(){
        Intent intent = new Intent(getActivity(), set_target.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void set_regular(){
        Intent intent = new Intent(getActivity(), mg_regular.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void log_out(){
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
    }
}