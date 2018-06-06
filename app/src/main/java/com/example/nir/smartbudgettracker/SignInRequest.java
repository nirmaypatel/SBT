package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 4/3/2016.
 */
public class SignInRequest extends StringRequest{
    private static final String SIGNIN_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/get_user.php";
    private Map<String,String> parameters;
    public SignInRequest(String username, String password, Response.Listener<String> listener){
        super(Method.POST,SIGNIN_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("username",username);
        parameters.put("password",password);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
