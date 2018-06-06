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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
public class Categories extends Fragment implements  View.OnClickListener {

    Button catadButton;
    HashMap <Integer, String> categoryMap = new HashMap<Integer, String>();

    public Categories() {
        // Required empty public constructor
    }
    public void onStart(){
        super.onStart();

    }
    void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int uid;
        final HashMap<Integer, String> catMap = new HashMap<Integer, String>();
        final ArrayList<String> catNames = new ArrayList<String>();
        Log.i("categories", "in categories on load");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid=settings.getInt("uid",-1);
        Log.i("uid", Integer.toString(uid));
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);
        final Categories that = this;

        Response.Listener listener= new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.i("category fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("category fragment", jsonObject.toString());

                    if (success) {
                        final  TableLayout tableLayout = (TableLayout) view.findViewById(R.id.catTable);
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);


                        final JSONArray catArr = jsonObject.getJSONArray("categories");

                        for(int i=0; i< catArr.length(); i++){

                            JSONObject obj = catArr.getJSONObject(i);
                            Log.i("category id", Integer.toString(obj.getInt("cat_id")));
                            Log.i("category name", obj.getString("cat_name"));
                            categoryMap.put(obj.getInt("cat_id"), obj.getString("cat_name"));
                            TableRow tr= new TableRow(getActivity());

                            TextView catName=new TextView(getActivity());
                            catName.setId(obj.getInt("cat_id"));
                            catName.setText(obj.getString("cat_name"));
                            catName.setPadding(5, 5, 5, 5);
                            catName.setWidth(650);
                            catName.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                            catName.setLayoutParams(layoutParams);
                            catName.setAllCaps(true);

                            tr.addView(catName);
                            final Button delButton = new Button(getActivity());
                            delButton.setId(obj.getInt("cat_id"));// define id that must be unique
                            delButton.setText("DELETE"); // set the text for the header
                            delButton.setOnClickListener(that);
                            delButton.setPadding(10, 5, 10, 5); // set the padding (if required)
                            delButton.setTextSize(getResources().getDimension(R.dimen.normal_text_size));
                            delButton.setLayoutParams(layoutParams);
                            delButton.setTextColor(getResources().getColor(R.color.colorRedButton));
                            delButton.setBackgroundColor(Color.WHITE);


                            tr.addView(delButton);
                            tr.setPadding(10, 10, 10, 10);
                            tableLayout.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                            catNames.add(obj.getString("cat_name"));

                        }




                    }

                } catch (JSONException e) {
                }

            }
        };
        GetCategoryRequest getCategoryRequest =new GetCategoryRequest(uid, listener);
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(getCategoryRequest);

        catadButton=(Button) view.findViewById(R.id.catAdButton);
        catadButton.setOnClickListener(this);

        return  view;
    }


    @Override
    public void onClick(View view) {
        final int uid;
        final HashMap<Integer, String> catMap = new HashMap<Integer, String>();
        if(view.getId()  == R.id.catAdButton){
            Log.i("test button", "add button clicked");
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
            uid=settings.getInt("uid",-1);
            Log.i("uid", Integer.toString(uid));
            Response.Listener listener=new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);
                    boolean success= jsonObject.getBoolean("success");
                    Log.i("category fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("category fragment", jsonObject.toString());

                    if (success) {
                        Log.i("category fragment", "in success");
                        final JSONArray catArr = jsonObject.getJSONArray("categories");
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("New Category");


                        final EditText catInput = new EditText(getActivity());
                        catInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        catInput.setHint("Add category");

                        LinearLayout expLayout = new LinearLayout(getActivity());
                        expLayout.setOrientation(LinearLayout.VERTICAL);

                        expLayout.addView(catInput);

                        alertDialog.setView(expLayout);
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                            "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                String cat_name = catInput.getText().toString();

                                Response.Listener listener=new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        Log.i("category fragment", "request string on server =" + jsonObject.getString("request"));
                                        Log.i("category fragmnet", jsonObject.toString());

                                        if (success) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Category Added").setPositiveButton("Ok", null).create().show();
                                            refreshFragment();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("Category Addition Failed").setNegativeButton("Retry", null).create().show();
                                        }
                                    } catch (JSONException e) {

                                    }
                                    }
                                };
                                AddCategoryRequest addCategoryRequest = new AddCategoryRequest(uid, cat_name, listener);
                                RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                                requestQueue.add(addCategoryRequest);
                                }
                            });

                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        alertDialog.show();
                    }else {
                        Log.e("Category Fragment", "in fail");
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
            final int delCatId = view.getId();
            String delCatName = "";
            for (HashMap.Entry<Integer, String> entry : categoryMap.entrySet()) {
                Integer catId = entry.getKey();
                String catName = entry.getValue();
                if(catId == delCatId){
                    delCatName = catName;
                }
            }
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Delete Category");

            alertDialog.setMessage("Do you want to delete " + delCatName + "?");

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
                            DeleteCategoryRequest deleteCategoryRequest = new DeleteCategoryRequest(delCatId, listener);
                            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
                            requestQueue.add(deleteCategoryRequest);
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
