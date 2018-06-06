package com.example.anu.smartbudgettracker;


import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.text.InputType;

import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment implements  View.OnClickListener{
    PieChart pieChart;
    Button adButton;
    //TextView textView;
    TextView tx;
    String[] s = {"Select Category", "travel ", "food", "rent"};

    public Dashboard() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    void refreshFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int uid;
        Log.i("dashboard", "in dashboard on load");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        uid = settings.getInt("uid", -1);
        Log.i("uid", Integer.toString(uid));
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("DashBoard fragmnet", jsonObject.toString());

                    if (success) {
                        pieChart = (PieChart) view.findViewById(R.id.pcPieChart);
                        final JSONArray catArr = jsonObject.getJSONArray("expenses");
                        ArrayList<Entry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<>();
                        for (int i = 0; i < catArr.length(); i++) {
                            JSONObject obj = catArr.getJSONObject(i);
                            entries.add(new Entry(obj.getInt("total_amt"), i));
                            labels.add(obj.getString("cat_name"));
                        }

                        PieDataSet pieDataSet = new PieDataSet(entries, "$");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        PieData pieData = new PieData(labels, pieDataSet);
                        pieData.setValueTextSize(20);
                        pieChart.setData(pieData);

                        pieChart.setDescription(" ");
                        pieChart.setDrawHoleEnabled(false);
                        //pieChart.setHoleColorTransparent(false);
                        Legend legend = pieChart.getLegend();
                        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);


                        pieChart.animateXY(300, 300);
                    } else {

                    }
                } catch (JSONException e) {

                }
            }
        };
        GetMonthlyExpenseRequest expenseRequest = new GetMonthlyExpenseRequest(uid, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(expenseRequest);

        Button logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent logoutIntent= new Intent(getActivity(), SignInActivity.class);
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("uid", -1);
        editor.commit();
        getActivity().startActivity(logoutIntent);
    }
}

