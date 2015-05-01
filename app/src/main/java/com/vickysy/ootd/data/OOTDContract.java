package com.vickysy.ootd.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the OOTDContract database.
 *
 * Note: outfit and outfit_item tables not yet used as of 05/01/2015
 * Created by vickysy on 3/10/15.
 */
public class OOTDContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.vickysy.ootd";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.vicky.ootd/ is a valid path for
    // looking at weather data. content://com.vicky.ootd/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_ITEM = "item";
    public static final String PATH_OUTFIT = "outfit";
    public static final String PATH_OUTFIT_ITEM = "outfit_item";


    /* Inner class that defines the table contents of the item table */
    public static final class ItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ITEM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEM;

        // Table name
        public static final String TABLE_NAME = "item";

        // The item type.
        public static final String COLUMN_ITEM_TYPE = "item_type";

        public static final String COLUMN_IMG_PATH = "img_path";

        // The brand of the item
        public static final String COLUMN_BRAND = "brand";

        // The condition of the item
        public static final String COLUMN_CONDITION = "condition";

        // The color of the item
        public static final String COLUMN_COLOR = "color";

        // The material of the item
        public static final String COLUMN_MATERIAL = "material";

        public static final String COLUMN_DATE_PURCHASED = "date_purchased";

        public static Uri buildItemUri() {
            return CONTENT_URI;
        }

        public static Uri buildItemUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static int getItemIdFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
    }

    /* Inner class that defines the table contents of the outfit table */
    public static final class OutfitEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTFIT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFIT;

        public static final String TABLE_NAME = "outfit";

        // Outfit name
        public static final String COLUMN_NAME = "name";

        public static final String COLUMN_CATEGORY = "category";

        public static Uri buildOutfitUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /* Inner class that defines the table contents of the outfit table */
    public static final class OutfitItemEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_OUTFIT_ITEM).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_OUTFIT;

        public static final String TABLE_NAME = "outfit_item";

        // Outfit name
        public static final String COLUMN_ITEM_KEY = "item_id";

        public static final String COLUMN_OUTFIT_KEY = "outfit_id";

        public static Uri buildOutfitUri() {
            return CONTENT_URI;
        }

        public static Uri buildOutfitUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
