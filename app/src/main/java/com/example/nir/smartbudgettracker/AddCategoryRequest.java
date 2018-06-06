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
public class AddCategoryRequest extends StringRequest {
    private static final String ADD_CATEGORY_REQ_URL = "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/add_category.php";
    private Map<String, String> parameters;

    public AddCategoryRequest(int uid, String cat_name, Response.Listener<String> listener) {
        super(Request.Method.POST, ADD_CATEGORY_REQ_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("uid", Integer.toString(uid));
        parameters.put("cat_name", cat_name);
        //parameters.put("", Integer.toString(expense));
        parameters.put("token", "token");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}