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
public class ItemTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = ItemTask.class.getSimpleName();

    private final Context mContext;

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
    long addItem(String itemType, String imagePath) {
        long itemId;
        ContentValues itemValues = new ContentValues();
        itemValues.put(OOTDContract.ItemEntry.COLUMN_ITEM_TYPE, itemType);
        itemValues.put(OOTDContract.ItemEntry.COLUMN_IMG_PATH, imagePath);
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
    int deleteItem(long itemId) {

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

    /**
     * Helper method to handle deletion of an item in the OOTD database.
     *
     * @param itemId
     * @return the rows deleted.
     */
    int editItem(long itemId, String itemType) {
        String mSelectionClause = OOTDContract.ItemEntry._ID+ " = ?";
        String[] mSelectionArgs = {"" + itemId};

        int mRowsUpdated = 0;

        // Defines an object to contain the updated values
        ContentValues mUpdateValues = new ContentValues();
        mUpdateValues.put(OOTDContract.ItemEntry.COLUMN_ITEM_TYPE, itemType);

        mRowsUpdated = mContext.getContentResolver().update(
                OOTDContract.ItemEntry.CONTENT_URI,
                mUpdateValues,                          // the columns to update
                mSelectionClause,                       // the column to select on
                mSelectionArgs                          // the value to compare to
        );

        return mRowsUpdated;
    }

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }
}
