package com.example.knwldg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class QuesDbOpenHelper extends SQLiteAssetHelper {

    //Open helper for question/tutorial database
    private static final String DATABASE_NAME = "KNWLDG_TUT_QUES.db";
    private static final int DATABASE_VERSION = 1;


    public QuesDbOpenHelper(Context context) {
        super(context, DATABASE_NAME,  null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
