package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 5/3/2016.
 */


public class GetGoalRequest  extends StringRequest {
    private static final String GET_GOAL_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/get_goals.php";
    private Map<String,String> parameters;
    public GetGoalRequest( int uid , Response.Listener<String> listener){
        super(Method.POST,GET_GOAL_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("uid",Integer.toString(uid));
        parameters.put("token", "token");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}