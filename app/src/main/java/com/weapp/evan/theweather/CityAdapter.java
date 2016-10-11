package com.weapp.evan.theweather;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.weapp.evan.theweather.db.DatabaseHelper;
import com.weapp.evan.theweather.entity.City;
import java.util.List;

/**
 * Created by Evan on 2016-09-25.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    public List<City> item;
    private Activity context;
    OnItemClickListener mItemClickListener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);

        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    public CityAdapter(List<City> item, Activity context) {

        this.item = item;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public void onBindViewHolder(final CityAdapter.MyViewHolder holder, final int position) {

        final City  city = item.get(position);
        holder.city.setText(city.getCityName());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView city;
        private ImageView city_remove;

        public MyViewHolder(final View itemView) {
            super(itemView);

            city = (TextView) itemView.findViewById(R.id.city_list_name);
            city_remove = (ImageView) itemView.findViewById(R.id.city_remove_icon);
            itemView.setOnClickListener(this);

            city_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {

                        DatabaseHelper db = new DatabaseHelper(v.getContext());
                        db.deleteOne(item.get(position).getCityName());
                        item.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){e.printStackTrace();}
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
