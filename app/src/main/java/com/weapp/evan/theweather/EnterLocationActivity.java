package com.weapp.evan.theweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.weapp.evan.theweather.utils.GooglePlacesAutocompleteAdapter;

import java.security.PublicKey;

public class EnterLocationActivity extends AppCompatActivity {

    ListView listView;
    GooglePlacesAutocompleteAdapter dataAdapter;
    EditText etEnterLocation;
    private static final String TAG = EnterLocationActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_location);

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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("location",location.replaceAll("\\s+", ""));
                i.putExtras(mBundle);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}
