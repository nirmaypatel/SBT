package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by developer on 5/3/2016.
 */



public class AddGoalRequest extends StringRequest {
    private static final String ADD_GOAL_REQ_URL = "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/add_goal.php";
    private Map<String, String> parameters;

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

    public AddGoalRequest(int cat_id, int goal, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_GOAL_REQ_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("cat_id", Integer.toString(cat_id));
        parameters.put("goal",Integer.toString(goal));
        //parameters.put("", Integer.toString(expense));
        parameters.put("token", "token");
    }



}