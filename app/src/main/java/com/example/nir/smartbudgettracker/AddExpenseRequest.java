package com.example.anu.smartbudgettracker;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anu on 5/1/2016.
 */
public class AddExpenseRequest extends StringRequest {
    private static final String ADD_EXPENSE_REQ_URL=  "http://ec2-52-38-31-40.us-west-2.compute.amazonaws.com/add_expense.php";
    private Map<String,String> parameters;

    public AddExpenseRequest(int categoryId, int expense, Response.Listener<String> listener){
        super(Request.Method.POST,ADD_EXPENSE_REQ_URL,listener,null);
        parameters= new HashMap<>();
        parameters.put("cat_id",Integer.toString(categoryId));
        parameters.put("expense", Integer.toString(expense));
        parameters.put("token","token");

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
