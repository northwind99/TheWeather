package com.weapp.evan.theweather;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weapp.evan.theweather.entity.Forecast;

import java.util.List;

/**
 * Created by Evan on 2016-09-25.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    private List<String> item;
    private Activity context;
    @Override
    public CityAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    public CityAdapter(List<String> item, Activity context) {

        this.item = item;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(CityAdapter.MyViewHolder holder, int position) {

            holder.city.setText(item.get(position));
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView city;
        public MyViewHolder(View itemView) {
            super(itemView);

            city = (TextView) itemView.findViewById(R.id.city_list_name);
        }
    }
}
