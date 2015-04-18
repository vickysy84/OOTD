package com.vickysy.ootd.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by vickysy on 3/10/15.
 */
public class OOTDProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private OOTDDbHelper mOpenHelper;

    static final int ITEM = 100;
    static final int ITEM_WITH_ID = 101;

    private static final SQLiteQueryBuilder sItemByIdQueryBuilder;

    static{
        sItemByIdQueryBuilder = new SQLiteQueryBuilder();

        sItemByIdQueryBuilder.setTables(
                OOTDContract.ItemEntry.TABLE_NAME);
    }

    private static final String sItemIdSelection =
            OOTDContract.ItemEntry.TABLE_NAME+
                    "." + OOTDContract.ItemEntry._ID + " = ? ";

    private Cursor getItemById(Uri uri, String[] projection, String sortOrder) {
        int id = OOTDContract.ItemEntry.getItemIdFromUri(uri);
        String[] selectionArgs;
        String selection;

        selectionArgs = new String[]{Integer.toString(id)};
        selection = sItemIdSelection;

        return sItemByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = OOTDContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, OOTDContract.PATH_ITEM, ITEM);
        matcher.addURI(authority, OOTDContract.PATH_ITEM + "/#", ITEM_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new OOTDDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ITEM:
                return OOTDContract.ItemEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {

            // "item"
            case ITEM: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        OOTDContract.ItemEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            // "item/#"
            case ITEM_WITH_ID: {
                retCursor = getItemById(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Items to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case ITEM: {
                long _id = db.insert(OOTDContract.ItemEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = OOTDContract.ItemEntry.buildItemUriWithId(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case ITEM:

                rowsDeleted = db.delete(
                        OOTDContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ITEM:
                rowsUpdated = db.update(OOTDContract.ItemEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEM:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(OOTDContract.ItemEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
