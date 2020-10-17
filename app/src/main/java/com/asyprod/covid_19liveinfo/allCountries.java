package com.asyprod.covid_19liveinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class allCountries extends AppCompatActivity {

    ListView countriesLV;
    EditText searchBar;
    SimpleArcLoader countryLoader;

    public static List<countryModel> countryModelList = new ArrayList<>();
    countryModel countryModels;
    customItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_countries);

        countriesLV = findViewById(R.id.countriesLV);
        searchBar = findViewById(R.id.searchBar);
        countryLoader = findViewById(R.id.countryLoader);

        getSupportActionBar().setTitle("Search for Countries");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itemAdapter.getFilter().filter(s);
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fetchData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchData(){
        String url = "https://corona.lmao.ninja/v2/countries";
        countryLoader.start();
        countriesLV.setVisibility(View.GONE);
        countryLoader.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(StringRequest.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray resultsArray = new JSONArray(response);
                    for(int i=0;i<resultsArray.length();i++){
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
                        countryModelList.add(countryModels);
                    }

                    itemAdapter = new customItemAdapter(allCountries.this,countryModelList);
                    countriesLV.setAdapter(itemAdapter);

                    countryLoader.stop();
                    countryLoader.setVisibility(View.GONE);
                    countriesLV.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    countryLoader.stop();
                    countryLoader.setVisibility(View.GONE);
                    countriesLV.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(allCountries.this, "An error ocurred, Please try again later!", Toast.LENGTH_SHORT).show();
                countryLoader.stop();
                countryLoader.setVisibility(View.GONE);
                countriesLV.setVisibility(View.VISIBLE);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(allCountries.this);
        requestQueue.add(request);

    }
}