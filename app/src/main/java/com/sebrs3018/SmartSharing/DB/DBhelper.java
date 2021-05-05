package com.sebrs3018.SmartSharing.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper
{
    public static final String DBNAME="smartsharing.db";

    public DBhelper(Context context) {
        super(context, DBNAME, null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String q="CREATE TABLE "+ DatabaseStrings.TBL_NAME +
                " (" +
                DatabaseStrings.FIELD_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DatabaseStrings.FIELD_EMAIL     + " TEXT UNIQUE NOT NULL," +
                DatabaseStrings.FIELD_PASSWORD  + " TEXT NOT NULL" +
                ")";

        db.execSQL(q);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    { }




}
