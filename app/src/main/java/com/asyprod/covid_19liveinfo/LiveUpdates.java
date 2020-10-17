package com.asyprod.covid_19liveinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LiveUpdates extends AppCompatActivity {

    TextView confirmed;
    TextView recovered;
    TextView active;
    TextView death;
    TextView todayCases;
    TextView todayDeath;

    SimpleArcLoader arcLoader;
    SimpleArcLoader countryLoader;
    LinearLayout worldPanel;
    PieChart pieChart;
    ListView sortedCountryLV;
    public static List<countryModel> countrySortedModelList = new ArrayList<>();
    countryModel countryModels;
    customItemAdapter adapter;
    LinearLayout allCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_updates);

        confirmed = findViewById(R.id.confirmedCases);
        recovered = findViewById(R.id.recovered);
        active = findViewById(R.id.active);
        death = findViewById(R.id.deaths);
        todayCases = findViewById(R.id.confirmedTodayCases);
        todayDeath = findViewById(R.id.todayDeaths);

        arcLoader = findViewById(R.id.loader);
        countryLoader = findViewById(R.id.Countryloader);
        worldPanel = findViewById(R.id.worldPanel);
        pieChart = findViewById(R.id.piechart);
        sortedCountryLV = findViewById(R.id.countriesLV);
        allCountries = findViewById(R.id.viewAll);

        TextView refreshWorldCases = findViewById(R.id.refreshWorldCases);
        TextView refreshCountryCases = findViewById(R.id.refreshCountryCases);
        refreshWorldCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWorldData();
            }
        });
        refreshCountryCases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchCountryData();
            }
        });

        getSupportActionBar().setTitle("LIVE UPDATES");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allCountries.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent allCountryIntent = new Intent(LiveUpdates.this,allCountries.class);
                startActivity(allCountryIntent);
            }
        });

        fetchWorldData();
        fetchCountryData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchWorldData(){
        String worldDataUrl = "https://corona.lmao.ninja/v2/all";
        pieChart.clearChart();
        worldPanel.setVisibility(View.GONE);
        arcLoader.setVisibility(View.VISIBLE);
        arcLoader.start();
            StringRequest request1 = new StringRequest(StringRequest.Method.GET, worldDataUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject results = new JSONObject(response);
                        confirmed.setText(results.getString("cases"));
                        recovered.setText(results.getString("recovered"));
                        active.setText(results.getString("active"));
                        death.setText(results.getString("deaths"));
                        todayCases.setText("[ + "+results.getString("todayCases")+"]");
                        todayDeath.setText("[ + "+results.getString("todayDeaths")+"]");

                        pieChart.addPieSlice(new PieModel("Confirmed", Integer.parseInt(results.getString("cases")), Color.parseColor("#D6368B")));
                        pieChart.addPieSlice(new PieModel("Deaths",Integer.parseInt(results.getString("deaths")),Color.parseColor("#FF0000")));
                        pieChart.addPieSlice(new PieModel("Recovered",Integer.parseInt(results.getString("recovered")),Color.parseColor("#30D593")));
                        pieChart.addPieSlice(new PieModel("Active",Integer.parseInt(results.getString("active")),Color.parseColor("#4297D8")));
                        pieChart.startAnimation();

                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                        worldPanel.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        arcLoader.stop();
                        arcLoader.setVisibility(View.GONE);
                        worldPanel.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LiveUpdates.this, "An Error occured, please try again!", Toast.LENGTH_SHORT).show();
                    arcLoader.stop();
                    arcLoader.setVisibility(View.GONE);
                    worldPanel.setVisibility(View.VISIBLE);
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request1);
        }

        public void fetchCountryData(){
            String sortedCountryUrl = "https://corona.lmao.ninja/v2/countries?sort=cases";
            countrySortedModelList.clear();
            countryLoader.setVisibility(View.VISIBLE);
            sortedCountryLV.setVisibility(View.GONE);
            countryLoader.start();
            StringRequest request2 = new StringRequest(StringRequest.Method.GET, sortedCountryUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray resultsArray = new JSONArray(response);
                        for(int i=0;i<5;i++){
                            JSONObject countryObj = resultsArray.getJSONObject(i);
                            String countryName = countryObj.getString("country");
                            String cases = countryObj.getString("cases");
                            String deaths = countryObj.getString("deaths");
                            String active = countryObj.getString("active");
                            String todayDeath = countryObj.getString("todayDeaths");
                            String todayCases = countryObj.getString("todayCases");
                            JSONObject countryInfoObj = countryObj.getJSONObject("countryInfo");
                            String flagUrl = countryInfoObj.getString("flag");

                            countryModels = new countryModel(flagUrl,countryName,cases,todayCases,active,deaths,todayDeath);
                            countrySortedModelList.add(countryModels);
                        }
                        adapter = new customItemAdapter(LiveUpdates.this,countrySortedModelList);
                        sortedCountryLV.setAdapter(adapter);

                        countryLoader.stop();
                        countryLoader.setVisibility(View.GONE);
                        sortedCountryLV.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        countryLoader.stop();
                        countryLoader.setVisibility(View.GONE);
                        sortedCountryLV.setVisibility(View.VISIBLE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(LiveUpdates.this, "An Error occured, please try again!", Toast.LENGTH_SHORT).show();
                    countryLoader.stop();
                    countryLoader.setVisibility(View.GONE);
                    sortedCountryLV.setVisibility(View.VISIBLE);
                }
            });

            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(request2);
        }
}