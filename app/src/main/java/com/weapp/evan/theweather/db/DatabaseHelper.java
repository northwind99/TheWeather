package com.weapp.evan.theweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.weapp.evan.theweather.entity.City;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "city_manager";

    // Contacts table name
    private static final String TABLE_NAME_CITY= "cities";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "city_name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
            String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_NAME_CITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";
        db.execSQL(CREATE_CITY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CITY);

        // Create tables again
        onCreate(db);
    }

    // Adding new city
    public void addCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, city.getCityName());

        // Inserting Row
        db.insert(TABLE_NAME_CITY, null, values);
        db.close(); // Closing database connection
    }

    // Getting single city
    public City getCity(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME_CITY, new String[] { KEY_ID,
                        KEY_NAME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        City city = new City(cursor.getString(0));
        // return contact
        return city;
    }

    // Getting All cities
    public List<City> getAllCities() {
        List<City>cityList = new ArrayList<City>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_CITY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                City city = new City((cursor.getString(0)));
                city.setId(Integer.parseInt(cursor.getString(0)));
                city.setCityName(cursor.getString(1));
                // Adding contact to list
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        // return city list
        return cityList;
    }

    public int getCitiesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME_CITY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating single event
    public int updateCity(City city) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, city.getCityName());

        // updating row
        return db.update(TABLE_NAME_CITY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(city.getId()) });
    }

    public void deleteRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CITY, null, null);
    }

    public int getCount(String cityName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = null;
        try {
            String query = "select * from " + TABLE_NAME_CITY + " where " + KEY_NAME + " = ?";
            c = db.rawQuery(query, new String[] {cityName});
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        }
        finally {
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME_CITY,null,null);
        db.execSQL("delete from "+ TABLE_NAME_CITY);
//        db.execSQL("TRUNCATE table " + TABLE_NAME_CITY);
        db.close();
    }

    public void deleteOne(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String table = TABLE_NAME_CITY;
        String whereClause = KEY_NAME + "=?";
        String[] whereArgs = new String[] { name };
        db.delete(table, whereClause, whereArgs);
        db.close();
    }

}
