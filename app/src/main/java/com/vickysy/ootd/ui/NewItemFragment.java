package com.vickysy.ootd.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vickysy.ootd.R;
import com.vickysy.ootd.data.OOTDContract;
import com.vickysy.ootd.service.ItemTask;
import com.vickysy.ootd.utils.camera.PhotoUtility;

import java.io.IOException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewItemFragment extends Fragment implements PopupMenu.OnMenuItemClickListener, View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>{

    static final String ITEM_URI = "URI";
    private Uri mUri;

    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_IMAGE_GALLERY = 3;

    private static final int ITEM_LOADER = 0;

    static final int NEW_ITEM = 0;
    static final int EDIT_ITEM = 1;

    private static final String ACTION = "action";
    private static final String ID = "id";
    private static final String IMAGE_BITMAP = "image_bitmap";
    private static final String TWO_PANE = "two_pane";

    private static final String[] DETAIL_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            OOTDContract.ItemEntry.TABLE_NAME + "." + OOTDContract.ItemEntry._ID,
            OOTDContract.ItemEntry.COLUMN_ITEM_TYPE,
            OOTDContract.ItemEntry.COLUMN_IMG_PATH,
            OOTDContract.ItemEntry.COLUMN_BRAND,
            OOTDContract.ItemEntry.COLUMN_CONDITION,
            OOTDContract.ItemEntry.COLUMN_COLOR,
            OOTDContract.ItemEntry.COLUMN_MATERIAL
    };

    // These indices are tied to ITEM_COLUMNS.  If ITEMS_COLUMNS changes, these
    // must change.
    static final int COL_ITEM_ID = 0;
    static final int COL_ITEM_TYPE = 1;
    static final int COL_IMG_PATH = 2;
    static final int COL_BRAND = 3;
    static final int COL_CONDITION = 4;
    static final int COL_COLOR = 5;
    static final int COL_MATERIAL = 6;

    private int action;
    private long id;
    private Bitmap mImageBitmap;
    private boolean mTwoPane;

    // Form elements
    private ImageView mImageView;
    private Spinner itemTypeSpinner;
    private EditText brandText;
    private EditText conditionText;
    private EditText colorText;
    private EditText materialText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param action Parameter 1.
     * @param id Parameter 2.
     * @param imageBitmap image to save
     * @return A new instance of fragment NewItemFragment.
     */
    public static NewItemFragment newInstance(int action, long id, Bitmap imageBitmap, boolean twoPane) {
        NewItemFragment fragment = new NewItemFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(NewItemFragment.ITEM_URI, OOTDContract.ItemEntry.buildItemUri());
        arguments.putInt(ACTION, action);
        arguments.putLong(ID, id);
        arguments.putBoolean(TWO_PANE, twoPane);
        arguments.putParcelable(NewItemFragment.IMAGE_BITMAP, imageBitmap);
        fragment.setArguments(arguments);

        return fragment;
    }

    public static NewItemFragment newInstance(int action, long id, boolean twoPane) {
        NewItemFragment fragment = new NewItemFragment();

        Bundle arguments = new Bundle();
        arguments.putParcelable(NewItemFragment.ITEM_URI, OOTDContract.ItemEntry.buildItemUriWithId(id));
        arguments.putInt(ACTION, action);
        arguments.putLong(ID, id);
        arguments.putBoolean(TWO_PANE, twoPane);
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
            mTwoPane = getArguments().getBoolean(NewItemFragment.TWO_PANE);
            if(action == NEW_ITEM) {
                mImageBitmap = getArguments().getParcelable(NewItemFragment.IMAGE_BITMAP);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            setImageBitmap((Bitmap) extras.get("data"));
        }  else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == getActivity().RESULT_OK) {
            // TODO: check if multiple images were selected
            Uri uri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    void setImageBitmap(final Bitmap bitmap) {
        mImageBitmap = bitmap;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                mImageView.setImageDrawable(null);
                mImageView.setImageBitmap(bitmap);
                mImageView.invalidate();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(NewItemFragment.ITEM_URI);
            action = arguments.getInt(NewItemFragment.ACTION);
            id = arguments.getLong(NewItemFragment.ID);
            mTwoPane = arguments.getBoolean(NewItemFragment.TWO_PANE);
            if (action == NEW_ITEM) {
                mImageBitmap = arguments.getParcelable(NewItemFragment.IMAGE_BITMAP);
            }
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new_item, container, false);

        mImageView = (ImageView) rootView.findViewById(R.id.imageView);
        mImageView.setOnLongClickListener( new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // take photo and set to mImageView
                // call camera intent
                PopupMenu popupMenu = new PopupMenu(getActivity(), v);
                popupMenu.setOnMenuItemClickListener(NewItemFragment.this);
                popupMenu.inflate(R.menu.menu_photo_chooser);
                popupMenu.show();

                return true;
            }
        } );

        TextView frameTitleView = (TextView) rootView.findViewById(R.id.frame_title_view);
        itemTypeSpinner = (Spinner) rootView.findViewById(R.id.item_type_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.item_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        itemTypeSpinner.setAdapter(adapter);

        brandText = (EditText) rootView.findViewById(R.id.brand_text);
        conditionText = (EditText) rootView.findViewById(R.id.condition_text);
        colorText = (EditText) rootView.findViewById(R.id.color_text);
        materialText = (EditText) rootView.findViewById(R.id.material_text);

        Button submitButton = (Button) rootView.findViewById(R.id.submit);
        submitButton.setOnClickListener(this);

        switch (action) {
            case NEW_ITEM: submitButton.setText("Submit");
                mImageView.setImageBitmap(mImageBitmap);
                if (frameTitleView != null){
                    frameTitleView.setText("New Item");
                }
                break;
            case EDIT_ITEM: submitButton.setText("Save");
                if (frameTitleView != null){
                    frameTitleView.setText("Edit Item");
                }
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
                    if(mImageBitmap != null) {
                        try {
                            imagePath = PhotoUtility.saveImage(getActivity(), mImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // add item to db
                        long itemId = iTask.addItem(itemTypeSpinner.getSelectedItem().toString(), imagePath, brandText.getText().toString(),
                                conditionText.getText().toString(), colorText.getText().toString(), materialText.getText().toString());
                        Intent intentMessage = new Intent();
                        intentMessage.putExtra("MESSAGE", "Success");
                        getActivity().setResult(itemId > 0?1:0, intentMessage);
                        if (!mTwoPane) {
                            getActivity().finish();
                        } else if (itemId > 0) {
                            Crouton.makeText(getActivity(), "New Item Added", Style.INFO).show();
                        }
                    }
                    else {
                        Crouton.makeText(getActivity(), "Please enter image.", Style.INFO).show();
                    }

                    break;
                case EDIT_ITEM :
                    String imagePath2 = "";
                    if(mImageBitmap != null) {
                        try {
                            imagePath2 = PhotoUtility.saveImage(getActivity(), mImageBitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    int count = iTask.editItem(id, itemTypeSpinner.getSelectedItem().toString(), imagePath2, brandText.getText().toString(),
                            conditionText.getText().toString(), colorText.getText().toString(), materialText.getText().toString());
                    Intent intentMessage2 = new Intent();
                    intentMessage2.putExtra("MESSAGE", "Success");
                    getActivity().setResult(count>0?1:0, intentMessage2);
                    if (!mTwoPane) {
                        getActivity().finish();
                    } else if (count > 0) {
                        Crouton.makeText(getActivity(), "Item Edited", Style.INFO).show();
                    }
                    break;
                default:

            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(ITEM_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onItemEdit() {
        // replace the uri, since the id has changed
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

            // Read image path from cursor
            if (mImageBitmap == null) {
                String imgPath = data.getString(COL_IMG_PATH);
                mImageBitmap = BitmapFactory.decodeFile(imgPath);
            }

            mImageView.setImageBitmap(mImageBitmap);

            String brand = data.getString(COL_BRAND);
            brandText.setText(brand);

            String condition = data.getString(COL_CONDITION);
            conditionText.setText(condition);

            String color = data.getString(COL_COLOR);
            colorText.setText(color);

            String material = data.getString(COL_MATERIAL);
            materialText.setText(material);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Photo chooser menu
     * Camera or Photo gallery
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_camera:
                if (PhotoUtility.isIntentAvailable(getActivity(), MediaStore.ACTION_IMAGE_CAPTURE)) {
                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                }
                return true;
            case R.id.item_gallery:
                Intent intent2 = new Intent();
                intent2.setType("image/*");
                // intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //TODO: Allow multiple inserts
                intent2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent2, "Select Picture"), REQUEST_IMAGE_GALLERY);
                return true;
        }

        return false;
    }


    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }
}
