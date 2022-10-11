package com.example.myway;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NoticeRequest extends StringRequest {

    final static private String URL = "http://ec2-54-89-49-232.compute-1.amazonaws.com/Notice.php"; // "http:// 퍼블릭 DSN 주소/Login.php";
    private Map<String, String> parameters;

    public NoticeRequest(Response.Listener<String> listener,Response.ErrorListener errorListener) {
        super(Method.POST,URL, listener, errorListener);

        parameters = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}