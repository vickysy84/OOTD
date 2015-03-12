package com.vickysy.ootd;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vickysy.ootd.data.OOTDContract;
import com.vickysy.ootd.utils.PhotoUtility;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    static final String ITEM_URI = "URI";
    private Uri mUri;

    private static final int ITEM_LOADER = 0;

    static final int NEW_ITEM = 0;
    static final int EDIT_ITEM = 1;

    private static final String ACTION = "action";
    private static final String ID = "id";
    private static final String IMAGE_BITMAP = "image_bitmap";

    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            OOTDContract.ItemEntry.TABLE_NAME + "." + OOTDContract.ItemEntry._ID,
            OOTDContract.ItemEntry.COLUMN_ITEM_TYPE,
            OOTDContract.ItemEntry.COLUMN_IMG_PATH
    };

    // These indices are tied to ITEM_COLUMNS.  If ITEMS_COLUMNS changes, these
    // must change.
    static final int COL_ITEM_ID = 0;
    static final int COL_ITEM_TYPE = 1;
    static final int COL_IMG_PATH = 2;

    private int action;
    private long id;
    private Bitmap mImageBitmap;

    // Form elements
    private Spinner itemTypeSpinner;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param action Parameter 1.
     * @param id Parameter 2.
     * @param imageBitmap image to save
     * @return A new instance of fragment NewItemFragment.
     */
    public static NewItemFragment newInstance(int action, long id, Bitmap imageBitmap) {
        NewItemFragment fragment = new NewItemFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(NewItemFragment.ITEM_URI, OOTDContract.ItemEntry.buildItemUri());
        arguments.putInt(ACTION, action);
        arguments.putLong(ID, id);
        arguments.putParcelable(NewItemFragment.IMAGE_BITMAP, imageBitmap);
        fragment.setArguments(arguments);

        return fragment;
    }

    public static NewItemFragment newInstance(int action, long id) {
        NewItemFragment fragment = new NewItemFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(NewItemFragment.ITEM_URI, OOTDContract.ItemEntry.buildItemUriWithId(id));
        arguments.putInt(ACTION, action);
        arguments.putLong(ID, id);
        fragment.setArguments(arguments);

        return fragment;
    }

    public NewItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUri = getArguments().getParcelable(NewItemFragment.ITEM_URI);
            action = getArguments().getInt(ACTION);
            id = getArguments().getLong(ID);
            if(action == NEW_ITEM) {
                mImageBitmap = getArguments().getParcelable(NewItemFragment.IMAGE_BITMAP);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(NewItemFragment.ITEM_URI);
            action = arguments.getInt(NewItemFragment.ACTION);
            id = arguments.getLong(NewItemFragment.ID);
            if (action == NEW_ITEM) {
                mImageBitmap = arguments.getParcelable(NewItemFragment.IMAGE_BITMAP);
            }
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_item, container, false);

        ImageView mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        TextView frameTitleView = (TextView) rootView.findViewById(R.id.frame_title_view);
        itemTypeSpinner = (Spinner) rootView.findViewById(R.id.item_type_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.item_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        itemTypeSpinner.setAdapter(adapter);

        Button submitButton = (Button) rootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(this);

        switch (action) {
            case NEW_ITEM: submitButton.setText("Submit");
                frameTitleView.setText("New Item");
                mImageView.setImageBitmap(mImageBitmap);
                break;
            case EDIT_ITEM: submitButton.setText("Save");
                frameTitleView.setText("Edit Item");
                break;
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Uri uri = mUri;
        if (null != uri) {
            ItemTask iTask = new ItemTask(getActivity());
            switch (action) {
                case NEW_ITEM :
                    // save photo
                    String imagePath = "";
                    try {
                        imagePath = PhotoUtility.saveImage(getActivity(), mImageBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // add item to db
                    long itemId = iTask.addItem(itemTypeSpinner.getSelectedItem().toString(), imagePath);
                    Intent intentMessage = new Intent();
                    intentMessage.putExtra("MESSAGE", "Success");
                    getActivity().setResult(0, intentMessage);
                    getActivity().finish();
                    break;
                case EDIT_ITEM :
                    int count = iTask.editItem(id, itemTypeSpinner.getSelectedItem().toString());
                    Intent intentMessage2 = new Intent();
                    intentMessage2.putExtra("MESSAGE", "Success");
                    getActivity().setResult(2, intentMessage2);
                    getActivity().finish();
                    break;
            }
        }
    }

    void onItemEdit() {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            Uri updatedUri = OOTDContract.ItemEntry.buildItemUriWithId(id);
            mUri = updatedUri;
            getLoaderManager().restartLoader(ITEM_LOADER, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            String itemType = data.getString(COL_ITEM_TYPE);
            UIUtilities.setSpinnerValue(itemTypeSpinner, itemType);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
