package com.sebrs3018.SmartSharing.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.sebrs3018.SmartSharing.Exceptions.InvalidPasswordException;
import com.sebrs3018.SmartSharing.Exceptions.UserNotFoundException;

import static com.sebrs3018.SmartSharing.DB.DatabaseStrings.FIELD_ID;

public class DbManager
{
    private final String TAG = "DbManager";
    private final DBhelper dbhelper;

    public DbManager(Context ctx)
    {
        dbhelper = new DBhelper(ctx);
    }

    public boolean insertUser(String email, String password)
    {
        SQLiteDatabase db = null;
        try{
            db = dbhelper.getWritableDatabase();
        }
        catch (SQLiteException e){
            Log.i("DBHelper", e.getMessage());
        }
        ContentValues cv = new ContentValues();
        cv.put(DatabaseStrings.FIELD_EMAIL, email);
        cv.put(DatabaseStrings.FIELD_PASSWORD, password);
        Log.i(TAG, "insertUser: Inserita password dell'utente -> " + password);
        try
        {
            if (db != null) {
                long idUser = db.insert(DatabaseStrings.TBL_NAME, null,cv);
                    if(idUser == -1)
                        return false;
            }

        }
        catch (SQLiteException sqle)
        {
            Log.i("DBHelper", sqle.getMessage());
            return false;
        }

        db.close();
        return true;
    }

    public boolean login(String email, String password) throws UserNotFoundException, InvalidPasswordException
    {
/*        String queryString = String.format("SELECT " + FIELD_ID + " FROM " + DatabaseStrings.TBL_NAME + " WHERE " + DatabaseStrings.FIELD_EMAIL + " = '%s' AND " +  DatabaseStrings.FIELD_PASSWORD + " = '%s'",
                            email,  DBUtils.md5(password));*/
        String queryString = String.format("SELECT " + FIELD_ID + " FROM " + DatabaseStrings.TBL_NAME +
                                            " WHERE " + DatabaseStrings.FIELD_EMAIL + " = '%s'", email);
        SQLiteDatabase db = null;
        try{
            db = dbhelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null);

            if(!cursor.moveToFirst())   //se non c'è nessun utente...
               throw new UserNotFoundException();

            int id = cursor.getInt(0);   //mi prendo l'id appena trovato
            cursor.close();

            queryString = String.format("SELECT " + FIELD_ID + " FROM " + DatabaseStrings.TBL_NAME +
                                        " WHERE " + FIELD_ID + " = " + id + " AND " + DatabaseStrings.FIELD_PASSWORD + " = '%s'", DBUtils.md5(password));

            cursor = db.rawQuery(queryString, null);
            if(!cursor.moveToFirst())
                throw new InvalidPasswordException();

            cursor.close();
            db.close();
        }
        catch (SQLiteException e){
            Log.i("DBHelper", e.getMessage());
        }

        return true;
    }





    public boolean delete(long id)
    {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        try
        {
            if (db.delete(DatabaseStrings.TBL_NAME, FIELD_ID+"=?", new String[]{Long.toString(id)})>0)
                return true;
            return false;
        }
        catch (SQLiteException sqle)
        {
            return false;
        }
    }
    public Cursor query()
    {
        Cursor crs = null;
        try
        {
            SQLiteDatabase db = dbhelper.getReadableDatabase();
            crs = db.query(DatabaseStrings.TBL_NAME, null, null, null, null, null, null, null);
        }
        catch(SQLiteException sqle)
        {
            return null;
        }
        return crs;
    }
}