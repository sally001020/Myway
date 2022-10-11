package com.example.myway;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PswChangeRequest extends StringRequest {
    final static private String URL = "http://ec2-54-89-49-232.compute-1.amazonaws.com/PasswordChange.php"; // "http:// 퍼블릭 DSN 주소/Login.php";
    private Map<String, String> parameters;

    public PswChangeRequest(String userPassword,String userNewPassword, Response.Listener<String> listener) {
        super(Request.Method.POST,URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userPassword", userPassword);
        parameters.put("userNewPassword", userNewPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
