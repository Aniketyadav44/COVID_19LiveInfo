package com.asyprod.covid_19liveinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

class customItemAdapter extends ArrayAdapter<countryModel> {

    private Context context;
    private List<countryModel> countryModelList;           //a list of all countries received from API call
    private List<countryModel> countryModelListFiltered;   //Creating a list of filtered list based on searched word

    public customItemAdapter(@NonNull Context context, @NonNull List<countryModel> countryModelList) {
        super(context, R.layout.custom_item_layout,countryModelList);
            this.context = context;
            this.countryModelList = countryModelList;
            this.countryModelListFiltered = countryModelList;    //Initially filtered list will contain all countries results
}

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater customInflater = LayoutInflater.from(parent.getContext());
        View view = customInflater.inflate(R.layout.custom_item_layout,null,false);

        TextView countryName = view.findViewById(R.id.tvContryName);
        TextView todayCasesCard = view.findViewById(R.id.todayCasesCard);
        TextView todayDeathCard = view.findViewById(R.id.todayDeathCard);
        TextView casesCard = view.findViewById(R.id.casesOfCard);
        TextView activeCard = view.findViewById(R.id.activeCasesOfCard);
        TextView deathCard = view.findViewById(R.id.deathsOfCard);
        ImageView flag = view.findViewById(R.id.flag);

        countryName.setText(countryModelListFiltered.get(position).getCountryName());
        todayCasesCard.setText(countryModelListFiltered.get(position).getTodayCases());
        todayDeathCard.setText(countryModelListFiltered.get(position).getTodayDeaths());
        casesCard.setText(countryModelListFiltered.get(position).getCases());
        activeCard.setText(countryModelListFiltered.get(position).getActive());
        deathCard.setText(countryModelListFiltered.get(position).getDeaths());
        Glide.with(context).load(countryModelListFiltered.get(position).getFlagUrl()).into(flag);

        return view;

    }

    @Override
    public int getCount() {
        return countryModelListFiltered.size();
    }

    @Nullable
    @Override
    public countryModel getItem(int position) {
        return countryModelListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){            //Checking if user has not entered any contraints i.e string input in search Bar
                    filterResults.count = countryModelList.size();            //If empty then filtered result will be same as containing all resukts from API call
                    filterResults.values = countryModelList;
                }
                else{
                    List<countryModel> resultsArray = new ArrayList<>();                    //Creating a temporary array to store filtered country models in for loop
                    String searchStr = constraint.toString().toLowerCase();               //String of user entered input
                    for(countryModel itemModel : countryModelList){
                        if(itemModel.getCountryName().toLowerCase().contains(searchStr)){
                            resultsArray.add(itemModel);
                        }
                        filterResults.count = resultsArray.size();
                        filterResults.values = resultsArray;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                countryModelListFiltered = (List<countryModel>) results.values;
                allCountries.countryModelList = (List<countryModel>) results.values;
                notifyDataSetChanged();
            }
        };

        return filter;
    }
}
