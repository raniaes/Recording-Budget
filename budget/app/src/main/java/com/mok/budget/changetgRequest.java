package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class changetgRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/change_target.php";
    private Map<String, String> map;


    public changetgRequest(String id, String target, int price, String date, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
        map.put("target", target);
        map.put("price", price + "");
        map.put("date", date);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
