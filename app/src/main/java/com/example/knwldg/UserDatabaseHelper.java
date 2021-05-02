package com.example.knwldg;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "myUser.db";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);   }

    //Method to create table on database
    @Override
    public void onCreate(SQLiteDatabase myDB) {

        myDB.execSQL("PRAGMA foreign_keys=ON");
        myDB.execSQL("create TABLE USER_PROFILE(email TEXT primary key, name TEXT, password TEXT)");
        myDB.execSQL("create TABLE GAME_PROFILE(eCoins INTEGER, passStreak INTEGER, distinctionStreak INTEGER, fullmarkStreak INTEGER, " +
                "addTime_5 INTEGER, addTime_10 INTEGER, addTime_15 INTEGER,  showHintBoost INTEGER, userEmail TEXT, FOREIGN KEY (userEmail) " +
                "REFERENCES USER_PROFILE(email))");

    }

    //Drop the table if already exists
    @Override
    public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {

        myDB.execSQL("DROP TABLE IF EXISTS USER_PROFILE");
        myDB.execSQL("DROP TABLE IF EXISTS GAME_PROFILE");
    }

    @Override
    public void onOpen(SQLiteDatabase myDB) {
        super.onOpen(myDB);
        if (!myDB.isReadOnly()) {
            // Enable foreign key constraints
            myDB.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    //for data insertion
    public Boolean insertUserData(String email, String name, String password) {
        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValuesUser = new ContentValues();
        contentValuesUser.put("email", email);
        contentValuesUser.put("name", name);
        contentValuesUser.put("password", password);

        long result = myDB.insert("user_profile", null, contentValuesUser);

        if(result == 1)
            return false;
        else
            return true;

    }

    public void insertGameData(String email) {
        SQLiteDatabase myDB = this.getWritableDatabase();

        ContentValues contentValuesGame = new ContentValues();
        contentValuesGame.put("eCoins", 0);
        contentValuesGame.put("passStreak", 0);
        contentValuesGame.put("distinctionStreak", 0);
        contentValuesGame.put("fullmarkStreak", 0);
        contentValuesGame.put("addTime_5", 0);
        contentValuesGame.put("addTime_10", 0);
        contentValuesGame.put("addTime_15", 0);
        contentValuesGame.put("showHintBoost", 0);
        contentValuesGame.put("userEmail", email);
        //insert into empty table with user email as foreign key
        myDB.insert("game_profile", null, contentValuesGame);

    }

    //Check user redundancy
    public Boolean checkUser(String email) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT  * FROM user_profile WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0 )
            return true;
        else
            return false;
    }

    //Authenticate user with password
    public Boolean checkPassword(String email, String password) {
        SQLiteDatabase myDB = this.getReadableDatabase();
        Cursor cursor = myDB.rawQuery("SELECT * FROM user_profile WHERE email = ? " +
                "AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //check on updating password
    public Boolean updatePassword(String email, String newPassword) {

        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);

        long result = myDB.update("user_profile", contentValues, "email = ?", new String[] {email});

        if(result == 1)
            return true;
        else
            return false;
    }

    //Update profile (name...)
    public Boolean updateProfile (String email, String newName) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", newName);

        long result = myDB.update("user_profile", contentValues, "email = ?", new String[] {email});

        if(result == 1)
            return true;
        else
            return false;
    }

    //Query user's name for displaying
    public Cursor fetchName (String email) {

        SQLiteDatabase myDB = this.getReadableDatabase();

        Cursor cursor = myDB.rawQuery("SELECT name FROM user_profile WHERE email = ?", new String[]{email});

        return cursor;

    }


    //Query user's current eCoins balance to use in
    //eShop and updating the balance after each game
    public Cursor fetch_eCoins (String email) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        Cursor cursor = myDB.rawQuery("SELECT eCoins FROM game_profile WHERE userEmail = ?", new String[]{email});

        return cursor;

    }

    //Query for updating eCoins balance
    public void update_eCoins (String email, int eCoinsAmount) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("eCoins", eCoinsAmount);

        myDB.update("game_profile", contentValues, "userEmail = ?", new String[] {email});

    }

    public Cursor fetchStatistics (String email) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        Cursor cursor = myDB.rawQuery("SELECT passStreak, distinctionStreak, fullmarkStreak FROM game_profile WHERE userEmail = ?", new String[]{email});

        return cursor;
    }

    public void updateStatistics (String email, int pass, int distinction, int fullmark) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("passStreak", pass);
        contentValues.put("distinctionStreak", distinction);
        contentValues.put("fullmarkStreak", fullmark);

        myDB.update ("game_profile", contentValues, "userEmail = ?", new String[]{email});
    }

    public void addBoost (String email, String boostName, int unit) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(boostName, unit);

        myDB.update("game_profile", contentValues, "userEmail = ?", new String[]{email});
    }


    public Cursor fetchUserBoost (String email) {

        SQLiteDatabase myDB = this.getWritableDatabase();

        Cursor cursor = myDB.rawQuery("SELECT addTime_5, addTime_10, addTime_15, showHintBoost FROM game_profile WHERE userEmail = ?", new String[]{email});

        return cursor;
    }

    public void updateBoost(String email, String boostType, int boostLeft) {
        SQLiteDatabase myDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(boostType, boostLeft);

        myDB.update("game_profile", contentValues, "userEmail = ?", new String[]{email});
    }
}
