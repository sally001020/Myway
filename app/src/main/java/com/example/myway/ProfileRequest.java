package com.example.myway;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends StringRequest {

    final static private String URL = "http://ec2-54-89-49-232.compute-1.amazonaws.com/ProfileAdd.php"; // "http:// 퍼블릭 DSN 주소/Login.php";
    private Map<String, String> parameters;

    public ProfileRequest(String img_file,String userName, Response.Listener<String> listener) {
        super(Method.POST,URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userName",userName);
        parameters.put("img_file", img_file);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}