package com.xianyu.myorder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OrderDBHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Orders";
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "myOrder.db";

    public OrderDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table if not exists " + TABLE_NAME + " (Id integer primary key autoIncrement, OrderName text, OrderPrice integer, OrderImage text, OrderType integer)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
