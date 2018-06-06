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
public class Expenses extends Fragment implements  View.OnClickListener{

    Button adButton;
    HashMap <Integer, String> expenseMap = new HashMap<Integer, String>();

    public Expenses() {
        // Required empty public constructor
    }

    void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int uid;
        //final HashMap<Integer, String> catMap = new HashMap<Integer, String>();
        final ArrayList<String> catNames = new ArrayList<String>();
        Log.i("categories", "in categories on load");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid=settings.getInt("uid",-1);
        Log.i("uid", Integer.toString(uid));
        final View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        final Expenses that = this;


        Response.Listener listener= new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    Log.i("expense fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("expense fragment", jsonObject.toString());

                    if (success) {



                        final JSONArray catArr = jsonObject.getJSONArray("expenses");
                        final TableLayout tableLayout1 = (TableLayout) view.findViewById(R.id.expTable);
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                        for(int i=0; i< catArr.length(); i++){

                            JSONObject obj = catArr.getJSONObject(i);
                            Log.i("expense id", Integer.toString(obj.getInt("ex_id")));
                            Log.i("category name", obj.getString("cat_name"));
                            expenseMap.put(obj.getInt("ex_id"), obj.getString("cat_name"));

                            TableRow tr= new TableRow(getActivity());
                            TextView catName=new TextView(getActivity());
                            catName.setId(obj.getInt("cat_id"));
                            catName.setText(obj.getString("cat_name"));
                            catName.setPadding(5, 5, 5, 5);
                            catName.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                            catName.setLayoutParams(layoutParams);
                            catName.setAllCaps(true);
                            tr.addView(catName);

                            TextView expense=new TextView(getActivity());
                            expense.setText(obj.getString("amt"));
                            expense.setPadding(5, 5, 5, 5);
                            expense.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                            expense.setLayoutParams(layoutParams);
                            expense.setAllCaps(true);
                            tr.addView(expense);

                            final Button delButton = new Button(getActivity());
                            delButton.setId(obj.getInt("ex_id"));// define id that must be unique
                            delButton.setText("DELETE"); // set the text for the header
                            delButton.setOnClickListener(that);
                            delButton.setPadding(10, 5, 10, 5); // set the padding (if required)
                            delButton.setTextColor(getResources().getColor(R.color.colorRedButton));
                            delButton.setBackgroundColor(Color.WHITE);
                            delButton.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                            delButton.setLayoutParams(layoutParams);
                            tr.setPadding(10, 10, 10, 10);
                            tr.addView(delButton);
                            tableLayout1.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                        }
                    }

                } catch (JSONException e) {
                }

            }
        };
        GetExpensesRequest getExpensesRequest=new GetExpensesRequest(uid, listener);
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(getExpensesRequest);

        adButton=(Button) view.findViewById(R.id.expAdButton);
        adButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        final int uid;
        final HashMap<Integer, String> catMap = new HashMap<Integer, String>();
        if(view.getId()  == R.id.expAdButton){
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
                        Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                        Log.i("DashBoard fragmnet", jsonObject.toString());

                        if (success) {
                            Log.i("Dashboard fragment", "in success");
                            final JSONArray catArr = jsonObject.getJSONArray("categories");
                            for(int i=0; i< catArr.length(); i++){
                                JSONObject obj = catArr.getJSONObject(i);
                                Log.i("category id", Integer.toString(obj.getInt("cat_id")));
                                Log.i("category name", obj.getString("cat_name"));
                                catMap.put(obj.getInt("cat_id"), obj.getString("cat_name"));
                                catNames.add(obj.getString("cat_name"));
                            }

                            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                            alertDialog.setTitle("New Expense");


                            final EditText exInput = new EditText(getActivity());
                            exInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            exInput.setHint("Add Expense");

                            LinearLayout expLayout = new LinearLayout(getActivity());
                            expLayout.setOrientation(LinearLayout.VERTICAL);

                            //exInput.setLayoutParams(lp);
                            expLayout.addView(exInput);
                            final ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, catNames);
                            TextView tx;
                            tx = new TextView(getActivity());
                            final Spinner sp = new Spinner(getActivity());
                            sp.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            sp.setAdapter(adp);
                            expLayout.addView(sp);
                            alertDialog.setView(expLayout);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                                    "OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Integer expense = Integer.parseInt(exInput.getText().toString());
                                            String selectedCategoryName = sp.getSelectedItem().toString();
                                            int selectedCatId = 0;
                                            for (HashMap.Entry<Integer, String> entry : catMap.entrySet()) {
                                                Integer catId = entry.getKey();
                                                String catNAme = entry.getValue();
                                                if(catNAme.equals(selectedCategoryName)){
                                                    selectedCatId = catId;
                                                }
                                            }
                                            Log.i("spinner selected", selectedCategoryName);
                                            Log.i("spinner selected id", Integer.toString(selectedCatId));
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
                                                            builder.setMessage("Expense Added").setPositiveButton("Ok", null).create().show();
                                                            refreshFragment();
                                                        } else {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                            builder.setMessage("Expense addition failed").setNegativeButton("Ok", null).create().show();
                                                        }
                                                    } catch (JSONException e) {

                                                    }
                                                }
                                            };
                                            AddExpenseRequest expenseRequest = new AddExpenseRequest(selectedCatId, expense, listener);
                                            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                                            requestQueue.add(expenseRequest);
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
            final int delExId = view.getId();
            String delCatName = "";
            for (HashMap.Entry<Integer, String> entry : expenseMap.entrySet()) {
                Integer exId = entry.getKey();
                String catName = entry.getValue();
                if(exId == delExId){
                    delCatName = catName;
                }
            }
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Delete Expense");

            alertDialog.setMessage("Do you want to delete expense for " + delCatName + "?");

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
                            DeleteExpenseRequest deleteExpenseRequest = new DeleteExpenseRequest(delExId, listener);
                            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                            requestQueue.add(deleteExpenseRequest);
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
