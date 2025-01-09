package com.example.scribble;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating the 'users' table
        db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If there's a database version update, drop the old table and recreate it
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    //Method to insert a new user
    public boolean insertUser(String email,String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO users (email,username, password) VALUES (?, ?, ?)";
        db.execSQL(query, new String[]{email,username, password});
        db.close();
        return true;
    }




    // Method to validate user credentials
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"id"};
        String selection = "username = ? AND password = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query("users", columns, selection, selectionArgs, null, null, null);
        boolean isValid = cursor != null && cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }
}
