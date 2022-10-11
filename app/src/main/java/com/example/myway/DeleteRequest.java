package com.example.myway;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {

    final static private String URL = "http://ec2-54-89-49-232.compute-1.amazonaws.com/Delete.php"; // "http:// 퍼블릭 DSN 주소/Login.php";
    private Map<String, String> parameters;

    public DeleteRequest(String userPassword, Response.Listener<String> listener) {
        super(Method.POST,URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userPassword", userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}