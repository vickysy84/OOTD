package com.vickysy.ootd.service;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.vickysy.ootd.data.OOTDContract;

/**
 * TODO: Create, edit, save and delete outfits.
 * Created by vickysy on 4/18/15.
 */
public class OutfitTask extends AsyncTask<String, Void, Void> {


    private final Context mContext;

    public OutfitTask(Context context) {
        mContext = context;
    }


    private static final String[] OUTFIT_COLUMNS = {
            OOTDContract.OutfitEntry.TABLE_NAME + "." + OOTDContract.OutfitEntry._ID,
            OOTDContract.OutfitEntry.COLUMN_NAME,
            OOTDContract.OutfitEntry.COLUMN_CATEGORY
    };

    private static final String[] OUTFIT_ITEM_COLUMNS = {
            OOTDContract.OutfitItemEntry.TABLE_NAME + "." + OOTDContract.OutfitItemEntry._ID,
            OOTDContract.OutfitItemEntry.COLUMN_ITEM_KEY,
            OOTDContract.OutfitItemEntry.COLUMN_OUTFIT_KEY
    };


    /**
     * Helper method to handle insertion of a new item in the OOTD database.
     *
     * @param outfitName
     * @parem itemIds
     * @return the row ID of the added item.
     */
    public long addOutfit(String outfitName, String category, long[] itemIds) {
        long itemId;
        ContentValues itemValues = new ContentValues();
        itemValues.put(OOTDContract.OutfitEntry.COLUMN_NAME, outfitName);
        itemValues.put(OOTDContract.OutfitEntry.COLUMN_CATEGORY, category);

        //ContentProviderOperation operation = new ContentProviderOperation()

        Uri insertedUri = mContext.getContentResolver().insert(
                OOTDContract.ItemEntry.CONTENT_URI,
                itemValues
        );
        itemId = ContentUris.parseId(insertedUri);
        return itemId;
    }

    @Override
    protected Void doInBackground(String... params) {
        //
        return null;
    }
}
