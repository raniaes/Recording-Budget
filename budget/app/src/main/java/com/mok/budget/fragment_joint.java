package com.mok.budget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class fragment_joint extends Fragment {

    private Bundle bundle;
    private String id;
    private ListView joint_list;
    private String[][] St_joint_list;
    private String[] list_item;
    private Button jit_create_btn;
    private jit_create_dialog jit_cre_dialog;
    private jit_adduser_dialog jit_adduser_dialog;
    private jit_deleteuser_dialog jit_deleteuser_dialog;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joint, container, false);

        bundle = getArguments();
        id = bundle.getString("id");

        joint_list = view.findViewById(R.id.joint_list);
        jit_create_btn= view.findViewById(R.id.jit_create_btn);

        joint_list();

        jit_create_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.jit_create_btn:
                        jit_cre_dialog = new jit_create_dialog(getContext(), id, new jit_create_dialog.DialogDismissListener() {
                            @Override
                            public void onDialogDismiss() {
                                joint_list();
                            }
                        });
                        jit_cre_dialog.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void joint_list(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        int count = jsonObject.getInt("count");

                        St_joint_list = new String[count][3];
                        list_item = new String[count];

                        for (int i = 0; i < count; i++){
                            St_joint_list[i][0] = jsonObject.getString("code" + (i+1));
                            St_joint_list[i][1] = jsonObject.getString("name" + (i+1));
                            St_joint_list[i][2] = jsonObject.getString("admin" + (i+1));

                            if (St_joint_list[i][2].equals("1")) {
                                list_item[i] = St_joint_list[i][1] + " Manager";
                            }else{
                                list_item[i] = St_joint_list[i][1] + " User";
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, list_item);
                        joint_list.setAdapter(adapter);

                        joint_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                onclickjoint(position);
                            }
                        });

                        joint_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                                PopupMenu popupMenu = new PopupMenu(getContext(), view);
                                MenuInflater inflater = popupMenu.getMenuInflater();
                                inflater.inflate(R.menu.joint_menu, popupMenu.getMenu());

                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.menu_item1:
                                                onclick_adduser(position);
                                                break;
                                            case R.id.menu_item2:
                                                onclick_deleteuser(position);
                                                break;
                                            case R.id.menu_item3:
                                                onclick_delete(position);
                                                break;
                                        }
                                        return true;
                                    }
                                });

                                popupMenu.show();

                                return true;
                            }
                        });
                    }else{
                        joint_list.setAdapter(null);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        jointlistRequest jointlistRequest = new jointlistRequest(id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(jointlistRequest);
    }

    public void onclickjoint(int count){
        Intent intent = new Intent(context, Main_scn.class);
        intent.putExtra("id", St_joint_list[count][0]);
        intent.putExtra("second_id", id);
        startActivity(intent);
    }

    public void onclick_adduser(int count){
        if (!St_joint_list[count][2].equals("1")){
            Toast.makeText(context, "do not have permission", Toast.LENGTH_SHORT).show();
        }else{
            jit_adduser_dialog = new jit_adduser_dialog(getContext(), St_joint_list[count][0], St_joint_list[count][1]);
            jit_adduser_dialog.show();
        }
    }

    public void onclick_deleteuser(int count){
        if (!St_joint_list[count][2].equals("1")){
            Toast.makeText(context, "do not have permission", Toast.LENGTH_SHORT).show();
        }else{
            jit_deleteuser_dialog = new jit_deleteuser_dialog(getContext(), St_joint_list[count][0]);
            jit_deleteuser_dialog.show();
        }
    }

    public void onclick_delete(int count){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success){
                        Toast.makeText(context, "Success!!", Toast.LENGTH_SHORT).show();
                        joint_list();
                    }else{
                        Toast.makeText(context, "fail!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        deletejointRequest deletejointRequest = new deletejointRequest(id, Integer.parseInt(St_joint_list[count][2]), St_joint_list[count][0], responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(deletejointRequest);
    }

}