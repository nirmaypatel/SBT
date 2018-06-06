package com.example.anu.smartbudgettracker;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Goal extends Fragment implements  View.OnClickListener{

    HashMap <Integer, String> goalMap = new HashMap<Integer, String>();
    HashMap <Integer, Integer> expenseMap = new HashMap<Integer, Integer>();
    Button goalAdButton;

    public Goal() {
        // Required empty public constructor
    }


    public void onStart(){
        super.onStart();

    }
    void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    int counter=0;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final int uid;
        Log.i("categories", "in categories on load");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid=settings.getInt("uid",-1);
        Log.i("uid", Integer.toString(uid));
        final View view = inflater.inflate(R.layout.fragment_goal, container, false);
        final Goal that = this;

        Response.Listener listener= new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.i("category fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("category fragment", jsonObject.toString());

                    if (success) {
                        final  TableLayout tableLayout1 = (TableLayout) view.findViewById(R.id.goalTable);
                        final TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        final JSONArray goalsArr = jsonObject.getJSONArray("goals");

                        Response.Listener listener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                                    Log.i("DashBoard fragmnet", jsonObject.toString());

                                    if (success) {
                                        final JSONArray expArr = jsonObject.getJSONArray("expenses");
                                        for(int i=0; i< expArr.length(); i++) {
                                            JSONObject expenseObj = expArr.getJSONObject(i);
                                            expenseMap.put(expenseObj.getInt("cat_id"), expenseObj.getInt("total_amt"));
                                        }
                                        for(int i=0; i< goalsArr.length(); i++){
                                            JSONObject obj = goalsArr.getJSONObject(i);
                                            if(obj.getInt("goal") != 0){
                                                Log.i("category id", Integer.toString(obj.getInt("cat_id")));
                                                Log.i("category name", obj.getString("cat_name"));
                                                Log.i("goal", obj.getString("goal"));
                                                int goal = obj.getInt("goal");
                                                int expense = getExpense(obj.getInt("cat_id"));
                                                TableRow tr= new TableRow(getActivity());



                                                TextView catName=new TextView(getActivity());
                                                catName.setId(obj.getInt("cat_id"));
                                                catName.setText(obj.getString("cat_name"));
                                                catName.setPadding(10, 10, 10, 10);
                                                catName.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                                                catName.setLayoutParams(layoutParams);
                                                catName.setAllCaps(true);
                                                tr.addView(catName);

                                                TextView goalVal=new TextView(getActivity());
                                                goalVal.setText(Integer.toString(obj.getInt("goal")));
                                                goalVal.setPadding(10, 10, 10, 10);
                                                goalVal.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                                                goalVal.setLayoutParams(layoutParams);
                                                goalVal.setAllCaps(true);
                                                tr.addView(goalVal);

                                                TextView statusVal = getStatus(expense, goal);
                                                statusVal.setLayoutParams(layoutParams);
                                                tr.addView(statusVal);

                                                Button delButton = new Button(getActivity());
                                                delButton.setId(obj.getInt("cat_id"));// define id that must be unique
                                                delButton.setText("DELETE"); // set the text for the header
                                                delButton.setOnClickListener(that);
                                                delButton.setPadding(10, 5, 10, 5); // set the padding (if required)
                                                delButton.setTextColor(getResources().getColor(R.color.colorRedButton));
                                                delButton.setBackgroundColor(Color.WHITE);
                                                delButton.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                                                delButton.setLayoutParams(layoutParams);
                                                //tr.setPadding(10, 10, 10, 10);
                                                tr.addView(delButton);


                                                tableLayout1.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

                                                // catNames.add(obj.getString("cat_name"));
                                                goalMap.put(obj.getInt("cat_id"), obj.getString("cat_name"));
                                            }
                                        }

                                    } else {

                                    }
                                } catch (JSONException e) {

                                }
                            }
                        };
                        GetMonthlyExpenseRequest expenseRequest = new GetMonthlyExpenseRequest(uid, listener);
                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                        requestQueue.add(expenseRequest);


                    }

                } catch (JSONException e) {
                }

            }
        };
        GetGoalRequest getgoalRequest=new GetGoalRequest(uid, listener);
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(getgoalRequest);

        goalAdButton =(Button) view.findViewById(R.id.goalAdButton);
        goalAdButton.setOnClickListener(this);

        return  view;
    }


    int getExpense(int selectedCatId){
        int expense = 0;
        for (HashMap.Entry<Integer, Integer> entry : expenseMap.entrySet()) {
            int catId = entry.getKey();
            if (catId == selectedCatId) {
                expense = entry.getValue();
            }
        }
        return expense;
    }

    TextView getStatus (int expense, int goal){
        Log.i("expense check", Integer.toString(expense));
        Log.i("goal check", Integer.toString(goal));
        TextView statusVal=new TextView(getActivity());
        //statusVal.setText(statusTxt);
        statusVal.setPadding(10, 10, 10, 10);
        statusVal.setTextSize(getResources().getDimension(R.dimen.normal_text_size));

        statusVal.setAllCaps(true);
        statusVal.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.accept, 0, 0, 0);
        if(expense > goal){
            Log.i("check", "in red");
            statusVal.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.exclamation, 0, 0, 0);
        }else if(expense > (0.8 * goal)){
            Log.i("check", "in yellow");
            statusVal.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.warning, 0, 0, 0);
        }else{
            Log.i("check", "in green");
        }

        return statusVal;
    }

    @Override
    public void onClick(View view) {

        final int uid;
        final HashMap<Integer, String> catMap = new HashMap<Integer, String>();
        if(view.getId()  == R.id.goalAdButton){
            final ArrayList<String> catNames = new ArrayList<String>();
            Log.i("dashboard", "in dashboard click add spending");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            uid=settings.getInt("uid",-1);
            Log.i("uid", Integer.toString(uid));
            Response.Listener listener=new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject= new JSONObject(response);
                        boolean success= jsonObject.getBoolean("success");
                        Log.i("goal fragment", "request string on server =" + jsonObject.getString("request"));
                        Log.i("goal fragmnet", jsonObject.toString());

                        if (success) {
                            Log.i("Goal fragment", "in success");
                            final JSONArray catArr = jsonObject.getJSONArray("categories");
                            for(int i=0; i< catArr.length(); i++){
                                JSONObject obj = catArr.getJSONObject(i);
                                Log.i("category id", Integer.toString(obj.getInt("cat_id")));
                                Log.i("category name", obj.getString("cat_name"));
                                catMap.put(obj.getInt("cat_id"), obj.getString("cat_name"));
                                catNames.add(obj.getString("cat_name"));
                            }

                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("Set Goal");


                            final EditText goalInput = new EditText(getActivity());
                            goalInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            goalInput.setHint("Add Goal");

                            LinearLayout goalLayout = new LinearLayout(getActivity());
                            goalLayout.setOrientation(LinearLayout.VERTICAL);

                            //exInput.setLayoutParams(lp);
                            goalLayout.addView(goalInput);
                            final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, catNames);
                            TextView tx;
                            tx = new TextView(getActivity());
                            final Spinner sp = new Spinner(getActivity());
                            sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            sp.setAdapter(adp);
                            goalLayout.addView(sp);
                            alertDialog.setView(goalLayout);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                    "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Integer goal = Integer.parseInt(goalInput.getText().toString());
                                            String selectedCategoryName = sp.getSelectedItem().toString();
                                            int selectedCatId = 0;
                                            for (HashMap.Entry<Integer, String> entry : catMap.entrySet()) {
                                                Integer catId = entry.getKey();
                                                String catName = entry.getValue();
                                                if (catName.equals(selectedCategoryName)) {
                                                    selectedCatId = catId;
                                                }
                                            }
                                            Log.i("spinner selected", selectedCategoryName);
                                            Log.i("spinner selected id", Integer.toString(selectedCatId));
                                            Response.Listener listener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        boolean success = jsonObject.getBoolean("success");
                                                        Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                                                        Log.i("DashBoard fragmnet", jsonObject.toString());

                                                        if (success) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setMessage("Goal Added").setPositiveButton("Ok", null).create().show();
                                                            refreshFragment();
                                                        } else {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setMessage("Goal addition failed").setNegativeButton("Retry", null).create().show();
                                                        }
                                                    } catch (JSONException e) {

                                                    }
                                                }
                                            };
                                            AddGoalRequest addGoalRequest = new AddGoalRequest(selectedCatId, goal, listener);
                                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                            requestQueue.add(addGoalRequest);
                                        }
                                    });

                            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialog.show();
                        }else {
                            Log.e("Dashboard Fragment", "in fail");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            GetCategoryRequest getCategoryRequest =new GetCategoryRequest(uid,listener);
            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
            requestQueue.add(getCategoryRequest);
        }else{
            Log.i("in delete expense", "delete button");
            final int delGoalId = view.getId();
            String delCatName = "";
            for (HashMap.Entry<Integer, String> entry : goalMap.entrySet()) {
                Integer catId = entry.getKey();
                String catName = entry.getValue();
                if(catId == delGoalId){
                    delCatName = catName;
                }
            }
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Delete Goal");

            alertDialog.setMessage("Do you want to delete goal set for " + delCatName + "?");

            LinearLayout expLayout = new LinearLayout(getActivity());
            expLayout.setOrientation(LinearLayout.VERTICAL);

            alertDialog.setView(expLayout);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Response.Listener listener=new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                                        Log.i("DashBoard fragmnet", jsonObject.toString());

                                        if (success) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Delete successful").setPositiveButton("Ok", null).create().show();
                                            refreshFragment();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Delete failed").setNegativeButton("Ok", null).create().show();
                                        }
                                    } catch (JSONException e) {

                                    }
                                }
                            };
                            DeleteGoalRequest deleteGoalRequest = new DeleteGoalRequest(delGoalId, listener);
                            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                            requestQueue.add(deleteGoalRequest);
                        }
                    });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });

            alertDialog.show();

        }

    }
}