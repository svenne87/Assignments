package se.svenne.assignment2a;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmDatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "alarmManager";
    // Contacts table name
    private static final String TABLE_ALARMS = "alarms";
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_ALARM_ID = "alarmId";
    private static final String KEY_MILLISECONDS = "milliseconds";
    private static final String KEY_REPEAT = "repeat";
    private static final String KEY_ALARM_STRING = "alarmString";

    public AlarmDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARMS_TABLE = "CREATE TABLE " + TABLE_ALARMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_ALARM_STRING + " TEXT," +
                KEY_ALARM_ID + " INTEGER," + KEY_REPEAT + " BOOLEAN," + KEY_MILLISECONDS + " LONG" + ")";

        db.execSQL(CREATE_ALARMS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new alarm
    void addAlarm(AlarmNotice alarmNotice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ALARM_STRING, alarmNotice.getAlarmTimeString()); //alarm string
        values.put(KEY_ALARM_ID, alarmNotice.getAlarmId()); //alarm id
        values.put(KEY_REPEAT, alarmNotice.getRepeat()); //boolean repeat
        values.put(KEY_MILLISECONDS, alarmNotice.getCalendarMilli()); //calendar in milliseconds

        // Inserting Row
        db.insert(TABLE_ALARMS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single alarm
    AlarmNotice getAlarm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ALARMS, new String[]{KEY_ID,
                KEY_ALARM_STRING, KEY_ALARM_ID, KEY_REPEAT, KEY_MILLISECONDS}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        AlarmNotice alarmNotice = new AlarmNotice(cursor.getInt(0),
                cursor.getString(1), cursor.getInt(2), Boolean.parseBoolean(cursor.getString(3)), cursor.getLong(4));
        // return alarm
        return alarmNotice;
    }

    // Getting All Alarms
    public List<AlarmNotice> getAllAlarms() {
        List<AlarmNotice> alarmList = new ArrayList<AlarmNotice>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_ALARMS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                AlarmNotice alarmNotice = new AlarmNotice();
                alarmNotice.setId(cursor.getInt(0));
                alarmNotice.setAlarmId(cursor.getInt(1));
                alarmNotice.setAlarmString(cursor.getString(2));
                alarmNotice.setCalendarMilliseconds(cursor.getLong(3));
                alarmNotice.setRepeat(Boolean.parseBoolean(cursor.getString(4)));

                // Adding alarm to list
                alarmList.add(alarmNotice);
            } while (cursor.moveToNext());
        }

        // return alarm list
        return alarmList;
    }

    // Updating single Alarm
    public int updateAlarm(AlarmNotice alarmNotice) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, alarmNotice.getId()); //id
        values.put(KEY_ALARM_ID, alarmNotice.getAlarmId()); //alarm id
        values.put(KEY_MILLISECONDS, alarmNotice.getCalendarMilli()); //calendar in milliseconds
        values.put(KEY_REPEAT, alarmNotice.getRepeat()); //boolean repeat
        values.put(KEY_ALARM_STRING, alarmNotice.getAlarmTimeString()); //alarm string

        // updating row
        return db.update(TABLE_ALARMS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmNotice.getId())});
    }

    // Deleting single Alarm
    public void deleteAlarm(AlarmNotice alarmNotice) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARMS, KEY_ID + " = ?",
                new String[]{String.valueOf(alarmNotice.getId())});
        db.close();
    }

    // Getting Alarm Count
    public int getAlarmCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ALARMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
