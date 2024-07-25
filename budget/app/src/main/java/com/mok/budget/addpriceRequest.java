package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class addpriceRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/add_price.php";
    private Map<String, String> map;


    public addpriceRequest(String id, String date, String name, boolean chose, int price, String Filename, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("date", date);
        map.put("name", name);
        if (chose){
            map.put("chose", 1 + "");
        }else{
            map.put("chose", 0 + "");
        }
        map.put("price", price + "");
        map.put("picture", Filename);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
