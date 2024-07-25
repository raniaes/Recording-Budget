package com.mok.budget;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class jointuserlistRequest extends StringRequest {
    //server URL setting
    final static private String URL = "http://sgm1991.dothome.co.kr/joint_userlist.php";
    private Map<String, String> map;


    public jointuserlistRequest(String code, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("code", code);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
