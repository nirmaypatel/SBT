package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 5/3/2016.
 */


public class DeleteGoalRequest  extends StringRequest {
    private static final String GET_GOAL_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/delete_goal.php";
    private Map<String,String> parameters;
    public DeleteGoalRequest( int cat_id , Response.Listener<String> listener){
        super(Method.POST,GET_GOAL_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("cat_id",Integer.toString(cat_id));
        parameters.put("token", "token");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}