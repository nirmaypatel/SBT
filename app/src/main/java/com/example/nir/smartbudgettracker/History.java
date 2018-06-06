package com.example.anu.smartbudgettracker;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// import com.github.mikephil.charting.charts.BarChart;


/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment implements  View.OnClickListener{
    BarChart barChart;
    public History() {
        // Required empty public constructor
    }

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
        final View view = inflater.inflate(R.layout.fragment_history, container, false);
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    Log.i("Dashboard fragment", "request string on server =" + jsonObject.getString("request"));
                    Log.i("DashBoard fragmnet", jsonObject.toString());

                    if (success) {
                        barChart = (BarChart) view.findViewById(R.id.pcBarChart);
                        final JSONArray catArr = jsonObject.getJSONArray("expenses");
                        ArrayList<BarEntry> entries = new ArrayList<>();
                        ArrayList<String> labels = new ArrayList<>();
                        for (int i = 0; i < catArr.length(); i++) {
                            JSONObject obj = catArr.getJSONObject(i);
                            entries.add(new BarEntry(obj.getInt("total_amt"), i));
                            Log.i("before month", getMonth(obj.getInt("month")));
                            labels.add(getMonth(obj.getInt("month")));
                        }

                        BarDataSet dataSet = new BarDataSet(entries, "$");
                        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                        BarData barData = new BarData(labels, dataSet);
                        barData.setValueTextSize(20);
                        barChart.setData(barData);

                        barChart.setDescription(" ");
                        //pieChart.setHoleColorTransparent(false);
                        Legend legend = barChart.getLegend();
                        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);


                        barChart.animateXY(300, 300);
                    } else {

                    }
                } catch (JSONException e) {

                }
            }
        };
        GetYearlyExpenseRequest expenseRequest = new GetYearlyExpenseRequest(uid, listener);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(expenseRequest);

        return view;
    }


    String getMonth(int i){
        String month = "";
        switch (i){
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
                month = "Dec";
                break;
        }


        return month;
    }


    @Override
    public void onClick(View v) {

    }
}