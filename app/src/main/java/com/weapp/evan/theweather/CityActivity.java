package com.weapp.evan.theweather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.database.DatabaseUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.weapp.evan.theweather.db.DatabaseHelper;
import com.weapp.evan.theweather.entity.City;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static com.weapp.evan.theweather.MainActivity.PREFS_KEY;
import static com.weapp.evan.theweather.MainActivity.PREFS_NAME;
import static com.weapp.evan.theweather.MainActivity.cityList;

public class CityActivity extends AppCompatActivity {

    static Context context;
    private RecyclerView mRecyclerView;
    private CityAdapter cityAdapter;
    public static List<City> cityList = new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();

    }

    private void initView(){
        context = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.city_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cityList = db.getAllCities();
        List<City> result = new ArrayList<>();
        Set<String> names = new HashSet<>();
        for( City city : cityList ) {
            if( names.add( city.getCityName() )){
                result.add( city );
            }
        }
        cityAdapter = new CityAdapter(result, this);
        cityAdapter.SetOnItemClickListener(new CityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                City city = cityAdapter.item.get(position);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("location", city);
                i.putExtras(mBundle);
                startActivity(i);
                finish();
            }
        });
        mRecyclerView.setAdapter(cityAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
}
