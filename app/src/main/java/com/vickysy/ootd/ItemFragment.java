package com.vickysy.ootd;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vickysy.ootd.data.OOTDContract;

/**
 * A fragment representing a list of Items.
 * interface.
 */
public class ItemFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String LOG_TAG = ItemFragment.class.getSimpleName();
    private ItemAdapter mItemAdapter;

    private GridView mGridView;
    private int mPosition = GridView.INVALID_POSITION;
    private boolean mUseGridLayout;

    private static final String SELECTED_KEY = "selected_item";

    private static final int ITEM_LOADER = 0;

    private static final String[] ITEM_COLUMNS = {
            OOTDContract.ItemEntry.TABLE_NAME + "." + OOTDContract.ItemEntry._ID,
            OOTDContract.ItemEntry.COLUMN_ITEM_TYPE,
            OOTDContract.ItemEntry.COLUMN_IMG_PATH,
            OOTDContract.ItemEntry.COLUMN_BRAND,
            OOTDContract.ItemEntry.COLUMN_CONDITION,
            OOTDContract.ItemEntry.COLUMN_COLOR,
            OOTDContract.ItemEntry.COLUMN_MATERIAL
    };

    static final int COL_ITEM_ID = 0;
    static final int COL_ITEM_TYPE = 1;
    static final int COL_IMG_PATH = 2;
    static final int COL_BRAND = 3;
    static final int COL_CONDITION = 4;
    static final int COL_COLOR = 5;
    static final int COL_MATERIAL = 6;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(Uri dateUri);
    }

    public ItemFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // The ItemAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mItemAdapter = new ItemAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.gridview_item);
        mGridView.setAdapter(mItemAdapter);
        // We'll call our MainActivity
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    ((Callback) getActivity())
                            .onItemSelected(OOTDContract.ItemEntry.buildItemUriWithId(cursor.getLong(COL_ITEM_ID)));
                }
                // add border to the item
                if (view.isSelected()){
                    view.setSelected(false);
                } else {
                    view.setSelected(true);
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        mItemAdapter.setUseGridLayout(mUseGridLayout);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortOrder = OOTDContract.ItemEntry.COLUMN_ITEM_TYPE + " ASC";

        Uri itemUri = OOTDContract.ItemEntry.buildItemUri();

        return new CursorLoader(getActivity(),
                itemUri,
                ITEM_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mItemAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mItemAdapter.swapCursor(null);
    }

    public void setUseGridLayout(boolean useGridLayout) {
        mUseGridLayout = useGridLayout;
        if (mItemAdapter != null) {
            mItemAdapter.setUseGridLayout(mUseGridLayout);
        }
    }
}