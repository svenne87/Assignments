package se.svenne.assignment2a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "countryManager";
    // Contacts table name
    private static final String TABLE_COUNTRIES = "countries";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_YEAR = "year";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRIES_TABLE = "CREATE TABLE " + TABLE_COUNTRIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_COUNTRY + " TEXT,"
                + KEY_YEAR + " TEXT" + ")";
        db.execSQL(CREATE_COUNTRIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTRIES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addCountry(Country contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTRY, contact.getCountry()); // Country
        values.put(KEY_YEAR, contact.getYear()); // Year

        // Inserting Row
        db.insert(TABLE_COUNTRIES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single contact
    Country getCountry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COUNTRIES, new String[]{KEY_ID,
                KEY_COUNTRY, KEY_YEAR}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Country country = new Country(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return country
        return country;
    }

    // Getting All Country
    public List<Country> getAllContacts() {
        List<Country> countryList = new ArrayList<Country>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COUNTRIES + " ORDER BY country, year";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setID(Integer.parseInt(cursor.getString(0)));
                country.setCountry(cursor.getString(1));
                country.setYear(cursor.getString(2));
                // Adding contact to list
                countryList.add(country);
            } while (cursor.moveToNext());
        }

        // return country list
        return countryList;
    }

    // Updating single Country
    public int updateCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COUNTRY, country.getCountry());
        values.put(KEY_YEAR, country.getYear());

        // updating row
        return db.update(TABLE_COUNTRIES, values, KEY_ID + " = ?",
                new String[]{String.valueOf(country.getID())});
    }

    // Deleting single Country
    public void deleteCountry(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUNTRIES, KEY_ID + " = ?",
                new String[]{String.valueOf(country.getID())});
        db.close();
    }

    // Getting Country Count
    public int getCountryCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COUNTRIES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
