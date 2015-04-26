package com.vickysy.ootd.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.vickysy.ootd.data.OOTDContract;

/**
 * Adds, Edits, Deletes, and Bulk inserts Fashion items.
 * Created by vickysy on 3/10/15.
 */
public class ItemTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = ItemTask.class.getSimpleName();

    private final Context mContext;

    private static final String[] ITEM_COLUMNS = {
            OOTDContract.ItemEntry.TABLE_NAME + "." + OOTDContract.ItemEntry._ID,
            OOTDContract.ItemEntry.COLUMN_ITEM_TYPE,
            OOTDContract.ItemEntry.COLUMN_IMG_PATH,
            OOTDContract.ItemEntry.COLUMN_BRAND,
            OOTDContract.ItemEntry.COLUMN_CONDITION,
            OOTDContract.ItemEntry.COLUMN_COLOR,
            OOTDContract.ItemEntry.COLUMN_MATERIAL
    };

    public ItemTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;

    /**
     * Helper method to handle insertion of a new item in the OOTD database.
     *
     * @param itemType
     * @return the row ID of the added item.
     */
    public long addItem(String itemType, String imagePath, String brand, String condition, String color, String material) {
        long itemId;
        ContentValues itemValues = new ContentValues();
        itemValues.put(OOTDContract.ItemEntry.COLUMN_ITEM_TYPE, itemType);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_IMG_PATH, imagePath);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_BRAND, brand);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_CONDITION, condition);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_COLOR, color);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_MATERIAL, material);
        Uri insertedUri = mContext.getContentResolver().insert(
                OOTDContract.ItemEntry.CONTENT_URI,
                itemValues
        );
        itemId = ContentUris.parseId(insertedUri);
        return itemId;
    }

    /**
     * Helper method to handle deletion of an item in the OOTD database.
     *
     * @param itemId
     * @return the rows deleted.
     */
    public int deleteItem(long itemId) {

        String mSelectionClause = OOTDContract.ItemEntry._ID+ " = ?";
        String[] mSelectionArgs = {"" + itemId};

        // Defines a variable to contain the number of rows deleted
        int mRowsDeleted = 0;

        mRowsDeleted = mContext.getContentResolver().delete(
                OOTDContract.ItemEntry.CONTENT_URI,
                mSelectionClause,                       // the column to select on
                mSelectionArgs                          // the value to compare to
        );

        return mRowsDeleted;
    }

    public int deleteItems(long[] itemIds) {
        String mSelectionClause = OOTDContract.ItemEntry._ID+ " IN (";
        String[] mSelectionArgs = new String[itemIds.length];
        for (int i = 0; i < itemIds.length; i++) {
            mSelectionClause += "?";
            if (i != itemIds.length -1) {
                mSelectionClause += ",";
            }
            mSelectionArgs[i] = "" + itemIds[i];
        }
        mSelectionClause += ")";

        // Defines a variable to contain the number of rows deleted
        int mRowsDeleted = 0;

        mRowsDeleted = mContext.getContentResolver().delete(
                OOTDContract.ItemEntry.CONTENT_URI,
                mSelectionClause,                       // the column to select on
                mSelectionArgs                          // the value to compare to
        );

        return mRowsDeleted;
    }

    /**
     * Helper method to handle deletion of an item in the OOTD database.
     *
     * @param itemId
     * @return the rows deleted.
     */
    public int editItem(long itemId, String itemType,  String imagePath, String brand, String condition, String color, String material) {
        String mSelectionClause = OOTDContract.ItemEntry._ID+ " = ?";
        String[] mSelectionArgs = {"" + itemId};

        int mRowsUpdated = 0;

        // Defines an object to contain the updated values
        ContentValues mUpdateValues = new ContentValues();
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_ITEM_TYPE, itemType);
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_IMG_PATH, imagePath);
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_BRAND, brand);
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_CONDITION, condition);
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_COLOR, color);
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_MATERIAL, material);

        mRowsUpdated = mContext.getContentResolver().update(
                OOTDContract.ItemEntry.CONTENT_URI,
                mUpdateValues,                          // the columns to update
                mSelectionClause,                       // the column to select on
                mSelectionArgs                          // the value to compare to
        );

        return mRowsUpdated;
    }

    /**
     * Gets the items with condition
     * @param itemIds
     * @return
     */
    public Cursor getItems(long[] itemIds) {

        String selectClause =  OOTDContract.ItemEntry.TABLE_NAME+
                "." + OOTDContract.ItemEntry._ID + " IN (";
        String[] selectionArgs = new String[itemIds.length];
        for (int i = 0; i < itemIds.length; i++) {
            selectClause += "?";
            if (i != itemIds.length -1) {
                selectClause += ",";
            }
            selectionArgs[i] = "" + itemIds[i];
        }
        selectClause += ")";

        Cursor cursor = mContext.getContentResolver().query(
                OOTDContract.ItemEntry.CONTENT_URI,
                ITEM_COLUMNS,
                selectClause,
                selectionArgs,
                null
        );

        return cursor;
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
