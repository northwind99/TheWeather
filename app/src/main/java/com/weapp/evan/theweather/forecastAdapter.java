package com.weapp.evan.theweather;

import android.app.Activity;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weapp.evan.theweather.entity.Forecast;

import java.util.List;

/**
 * Created by Evan on 2016-06-29.
 */
public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyViewHolder> {

    private List<Forecast> item;
    private Activity context;

    public ForecastAdapter(Activity context, List<Forecast> item) {
        this.item = item;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Forecast forecast =item.get(position);
        holder.date.setText(forecast.getDate());
        holder.max.setText(Integer.toString(forecast.getTemp_max()));
        holder.min.setText(Integer.toString(forecast.getTemp_min()));

        String icon = forecast.getIcon();

        if(icon.equals("01d")) {
            Picasso.with(context).load(R.drawable.sunny).into(holder.iconView);
        }else if(icon.equals("02d")) {
            Picasso.with(context).load(R.drawable.dfewcloud).into(holder.iconView);
        }else if(icon.equals("03d")) {
            Picasso.with(context).load(R.drawable.clouds).into(holder.iconView);
        }else if(icon.equals("04d")) {
            Picasso.with(context).load(R.drawable.clouds).into(holder.iconView);
        }else if(icon.equals("09d")) {
            Picasso.with(context).load(R.drawable.rain).into(holder.iconView);
        }else if(icon.equals("10d")) {
            Picasso.with(context).load(R.drawable.rain).into(holder.iconView);
        }else if(icon.equals("11d")) {
            Picasso.with(context).load(R.drawable.dstorm).into(holder.iconView);
        }else if(icon.equals("13d")) {
            Picasso.with(context).load(R.drawable.dsnowing).into(holder.iconView);
        }else if(icon.equals("50d")) {
            Picasso.with(context).load(R.drawable.dhaze).into(holder.iconView);
        }else if(icon.equals("01n")) {
            Picasso.with(context).load(R.drawable.moon).into(holder.iconView);
        }else if(icon.equals("02n")) {
            Picasso.with(context).load(R.drawable.nfewcloud).into(holder.iconView);
        }else if(icon.equals("03n")) {
            Picasso.with(context).load(R.drawable.clouds).into(holder.iconView);
        }else if(icon.equals("04n")) {
            Picasso.with(context).load(R.drawable.clouds).into(holder.iconView);
        }else if(icon.equals("09n")) {
            Picasso.with(context).load(R.drawable.rain).into(holder.iconView);
        }else if(icon.equals("10n")) {
            Picasso.with(context).load(R.drawable.rain).into(holder.iconView);
        }else if(icon.equals("11n")) {
            Picasso.with(context).load(R.drawable.nstorm).into(holder.iconView);
        }else if(icon.equals("13n")) {
            Picasso.with(context).load(R.drawable.nsnow).into(holder.iconView);
        }else if(icon.equals("50n")) {
            Picasso.with(context).load(R.drawable.nhaze).into(holder.iconView);
        }

    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private ImageView iconView;
        private TextView max;
        private TextView min;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date_to_week);
            iconView = (ImageView) itemView.findViewById(R.id.forecast_icon);
            max = (TextView) itemView.findViewById(R.id.forecast_max);
            min = (TextView) itemView.findViewById(R.id.forecast_min);
        }
    }
}
