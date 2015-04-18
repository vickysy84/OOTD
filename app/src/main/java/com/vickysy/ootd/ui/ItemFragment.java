package com.vickysy.ootd.ui;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vickysy.ootd.R;
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
        // use it to populate the GridView it's attached to.
        mItemAdapter = new ItemAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.gridview_item);
        mGridView.setAdapter(mItemAdapter);
        mGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        mGridView.setMultiChoiceModeListener(new MultiChoiceModeListener());

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

    public class MultiChoiceModeListener implements GridView.MultiChoiceModeListener {

        long selectedId = -1;
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Select Items");
            mode.setSubtitle("One item selected");
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_item_select, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:
                   // deleteSelectedItems();
                    ((MainActivity)getActivity()).deleteSelectedItem(selectedId);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.menu_edit:
                    ((MainActivity)getActivity()).editSelectedItem(selectedId);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id,
                                              boolean checked) {

            selectedId = id;
            int selectCount = mGridView.getCheckedItemCount();

            switch (selectCount) {
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + selectCount + " items selected");
                    break;
            }
        }

    }

}