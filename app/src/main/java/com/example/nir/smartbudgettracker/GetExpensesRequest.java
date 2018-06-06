package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 5/3/2016.
 */
public class GetExpensesRequest extends StringRequest {
    private static final String GET_EXPENSES_REQ_URL = "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/get_expenses.php";
    private Map<String, String> parameters;

    public GetExpensesRequest(int uid, Response.Listener<String> listener) {
        super(Request.Method.POST, GET_EXPENSES_REQ_URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("uid", Integer.toString(uid));
        parameters.put("token", "token");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
