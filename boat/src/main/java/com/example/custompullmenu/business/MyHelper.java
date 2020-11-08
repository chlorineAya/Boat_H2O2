package com.example.custompullmenu.business;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by louliang on 2018/05/05.
 */

public class MyHelper extends SQLiteOpenHelper {

    public MyHelper(Context context) {
        super(context,"hayden.db",null,3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE account(_id INTEGER PRIMARY KEY AUTOINCREMENT,phone VARCHAR(20),name VARCHAR(20),time INTEGER(100),fullName VARCHAR(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
