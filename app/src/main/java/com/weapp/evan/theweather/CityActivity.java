package com.weapp.evan.theweather;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class CityActivity extends AppCompatActivity {

    static Context context;
    private RecyclerView mRecyclerView;
    private CityAdapter cityAdapter;
    public static List<String> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();

    }

    private void initView(){
        context = this;

        Intent i = getIntent();
        cityList.add(i.getStringExtra("location"));
        mRecyclerView = (RecyclerView) findViewById(R.id.city_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityAdapter = new CityAdapter(cityList, this);
        mRecyclerView.setAdapter(cityAdapter);

    }
}
