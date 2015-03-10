package com.vickysy.ootd;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.vickysy.ootd.data.OOTDContract;

/**
 * Created by vickysy on 3/10/15.
 */
public class InsertItemTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = InsertItemTask.class.getSimpleName();

    private final Context mContext;

    public InsertItemTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param itemType
     * @return the row ID of the added item.
     */
    long addItem(String itemType) {
        long itemId;

//        Cursor itemCursor = mContext.getContentResolver().query(
//                OOTDContract.ItemEntry.CONTENT_URI,
//                new String[]{OOTDContract.ItemEntry._ID},
//                OOTDContract.ItemEntry.COLUMN_ITEM_TYPE + " = ?",
//                new String[]{itemType},
//                null);
//
//        if (itemCursor.moveToFirst()) {
//            int itemIdIndex = itemCursor.getColumnIndex(OOTDContract.ItemEntry._ID);
//            itemId = itemCursor.getLong(itemIdIndex);
//        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues itemValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            itemValues.put(OOTDContract.ItemEntry.COLUMN_ITEM_TYPE, itemType);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    OOTDContract.ItemEntry.CONTENT_URI,
                    itemValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            itemId = ContentUris.parseId(insertedUri);

//        }

       // itemCursor.close();
        return itemId;
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
