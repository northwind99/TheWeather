package com.weapp.evan.theweather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.weapp.evan.theweather.db.DatabaseHelper;
import com.weapp.evan.theweather.entity.City;
import com.weapp.evan.theweather.utils.GooglePlacesAutocompleteAdapter;

import java.security.PublicKey;

public class EnterLocationActivity extends AppCompatActivity {

    ListView listView;
    GooglePlacesAutocompleteAdapter dataAdapter;
    EditText etEnterLocation;
    private static final String TAG = EnterLocationActivity.class.getName();
    Context context;
    DatabaseHelper db = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);
        context =this;

        dataAdapter = new GooglePlacesAutocompleteAdapter(EnterLocationActivity.this, R.layout.listitem);

        listView= (ListView) findViewById(R.id.listView1);
        etEnterLocation = (EditText) findViewById(R.id.edEnterLocation);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //enables filtering for the contents of the given ListView
        listView.setTextFilterEnabled(true);

        etEnterLocation.addTextChangedListener(new

            TextWatcher() {
                public void afterTextChanged (Editable s){
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.v(TAG, "show results");
                    dataAdapter.getFilter().filter(s.toString());

                }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location=(String)parent.getItemAtPosition(position);
                City city = new City(location);
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("location",city);
                i.putExtras(mBundle);
            //    db.deleteAll();
                if (db.getCount(location) == 0) {
                    //perform inserting
                    db.addCity(city);
                }
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

}
