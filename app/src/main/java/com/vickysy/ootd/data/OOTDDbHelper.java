package com.vickysy.ootd.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vickysy.ootd.data.OOTDContract.ItemEntry;

/**
 *
 * Created by vickysy on 3/10/15.
 */
public class OOTDDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "ootd.db";

    public OOTDDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold items.
        final String SQL_CREATE_ITEM_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                ItemEntry._ID + " INTEGER PRIMARY KEY," +
                ItemEntry.COLUMN_ITEM_TYPE + " TEXT NOT NULL, " +
                ItemEntry.COLUMN_IMG_PATH + " TEXT NOT NULL " +
                " );";

        db.execSQL(SQL_CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME);
        onCreate(db);
    }
}
