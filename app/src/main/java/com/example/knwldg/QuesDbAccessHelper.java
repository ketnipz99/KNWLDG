package com.example.knwldg;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;


public class QuesDbAccessHelper {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static QuesDbAccessHelper instance;

    //Constructor of the class
    QuesDbAccessHelper(Context context) {
        this.openHelper = new QuesDbOpenHelper(context);
    }

    public static QuesDbAccessHelper getInstance(Context context) {

        if (instance == null) {
            instance = new QuesDbAccessHelper(context);
        }
        return instance;
    }

    public void openDatabase() {

        this.db = openHelper.getWritableDatabase();
    }

    //Close the database connection
    public void closeDatabase() {

        if (db != null) {
            db.close();
        }
    }

    //Query database for tutorial
    public List<Tutorial>  getTutorial(String cat) {

        List<Tutorial> tutorialList = new ArrayList<>();

        db = openHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tutorial.description, tutorial.image, question.ques_id as quesID FROM tutorial INNER JOIN question ON tutorial.question_id = quesID " +
                "WHERE question.category = ?  ORDER BY RANDOM() LIMIT 10", new String[]{cat});

        if (cursor.moveToFirst()) {
            do {
                Tutorial tutorial = new Tutorial();
                tutorial.setTutorial(cursor.getString(0));
                tutorial.setImage(cursor.getBlob(1));       //query image here
                tutorial.setQues_id(cursor.getInt(2));
                tutorialList.add(tutorial);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tutorialList;
    }

    public List<Tutorial>  getRandomTutorial() {

        List<Tutorial> tutorialList = new ArrayList<>();

        db = openHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tutorial.description, tutorial.image, question.ques_id as quesID FROM tutorial INNER JOIN question ON tutorial.question_id = quesID " +
                "ORDER BY RANDOM() LIMIT 10", new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Tutorial tutorial = new Tutorial();
                tutorial.setTutorial(cursor.getString(0));
                //query image here
                tutorial.setImage(cursor.getBlob(1));
                tutorial.setQues_id(cursor.getInt(2));
                tutorialList.add(tutorial);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return tutorialList;
    }


    //Query database for questionList
    public List<Question> getQuestion (int[] ID) {

        List<Question> questionList = new ArrayList<>();
        db = openHelper.getWritableDatabase();

        //Convert ques_ID to string to query in rawQuery
        String inQuesID = Arrays.toString(ID);
        //replace the symbols to allow query to read the IDs stored inside
        inQuesID = inQuesID.replace("[", "(");
        inQuesID = inQuesID.replace("]", ")");


        Cursor cursor = db.rawQuery("SELECT * FROM question WHERE ques_id IN " +inQuesID, new String[]{});

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(cursor.getString(1));
                question.setOption_1(cursor.getString(2));
                question.setOption_2(cursor.getString(3));
                question.setOption_3(cursor.getString(4));
                question.setOption_4(cursor.getString(5));
                question.setAnswer(cursor.getInt(6));
                //Add question to questionList
                questionList.add(question);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return questionList;
    }
}
