package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class weeklylistRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/weeklylist.php";
    private Map<String, String> map;


    public weeklylistRequest(String id, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("id", id);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
