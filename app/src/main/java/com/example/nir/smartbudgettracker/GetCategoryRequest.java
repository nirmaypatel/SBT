package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 5/1/2016.
 */
public class GetCategoryRequest extends StringRequest{
    private static final String ADD_EXPENSE_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/get_categories.php";
    private Map<String,String> parameters;
    public GetCategoryRequest(int uid, Response.Listener<String> listener){
        super(Method.POST,ADD_EXPENSE_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("uid",Integer.toString(uid));
        parameters.put("token", "token");
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
