package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class addweeklyRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/event_add.php";
    private Map<String, String> map;


    public addweeklyRequest(String id, String date, String name, boolean chose, int price, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("startdate", date);
        map.put("name", name);
        if (chose){
            map.put("chose", 1 + "");
        }else{
            map.put("chose", 0 + "");
        }
        map.put("price", price + "");
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
