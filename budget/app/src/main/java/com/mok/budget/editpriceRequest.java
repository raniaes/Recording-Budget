package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class editpriceRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/edit_price.php";
    private Map<String, String> map;


    public editpriceRequest(String id, int chose, String date, String re_name, int price, String name, String picture, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("chose", chose + "");
        map.put("date", date);
        map.put("name", name);
        map.put("price", price + "");
        map.put("re_name", re_name);
        map.put("picture", picture);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
