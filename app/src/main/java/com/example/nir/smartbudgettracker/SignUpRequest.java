package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 4/3/2016.
 */
public class SignUpRequest extends StringRequest {
    private static final String SIGNUP_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/create_user.php";
    private Map<String,String> parameters;
    public SignUpRequest(String fName, String lName, String email,int age, String username, String password, Response.Listener<String> listener){
        super(Method.POST,SIGNUP_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("fname",fName);
        parameters.put("lname",lName);
        parameters.put("email",email);
        parameters.put("age",age+"");
        parameters.put("username",username);
        parameters.put("password",password);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
