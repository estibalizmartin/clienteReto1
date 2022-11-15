package com.example.clientereto1.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.clientereto1.models.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Users.db";
    public static final String TABLE_NAME = "User";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (username TEXT PRIMARY KEY, password TEXT)");
        } catch (SQLiteException e) {
            try {
                throw new IOException(e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean createUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    @SuppressLint("Range")
    public User getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        User user = new User ();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }

/*    @SuppressLint("Range")
    public User getUser(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * FROM " + TABLE_NAME + " WHERE username = '" + username + "'", null);

        User user = new User ();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            cursor.close();
        } else {
            user = null;
        }
        db.close();
        return user;
    }*/

    public int deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        int query = db.delete(TABLE_NAME, null, null);
        db.close();
        return query;
    }

    public boolean isEmpty(){
        boolean ret = true;
        Cursor cursor = null;
        try {
            SQLiteDatabase sQLiteDatabase = this.getReadableDatabase();
            cursor = sQLiteDatabase.rawQuery( "SELECT count(*) FROM (select 0 from " +
                    TABLE_NAME + " limit 1)", null );
            cursor.moveToFirst();
            int count = cursor.getInt( 0 );
            if (count > 0) {
                ret = false;
            }
        } catch (Exception e) {
            // Nothing to do here...
        }
        finally {
            try {
                assert cursor != null;
                cursor.close();
            } catch (Exception e) {
                // Nothing to do here...
            }
        }
        return ret;
    }
}
