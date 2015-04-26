package com.vickysy.ootd.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import com.github.clans.fab.FloatingActionButton;
import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.data.CollageRegionData;
import com.redmadrobot.azoft.collage.ui.activities.CollageBuilderActivity;
import com.redmadrobot.azoft.collage.utils.CollageRegion;
import com.redmadrobot.azoft.collage.utils.collagegenerators.CollageFactory;
import com.redmadrobot.azoft.collage.utils.collagegenerators.SimpleCollageGenerator;
import com.vickysy.ootd.R;
import com.vickysy.ootd.service.ItemTask;
import com.vickysy.ootd.utils.camera.PhotoUtility;

import java.io.File;
import java.io.IOException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class MainActivity extends ActionBarActivity implements ItemFragment.Callback{

    private static final int NEW_ITEM = 0;
    private static final int EDIT_ITEM = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_IMAGE_GALLERY = 3;

    private static final int SUCCESS = 1;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    static final int FROM_CAMERA = 0;
    static final int FROM_GALLERY = 1;
    private boolean mTwoPane;

    @InjectSavedState
    private CollageFillData mCollageFillData;

    private GridView mGridView;

    private static final CollageFactory COLLAGE_FACTORY = new SimpleCollageGenerator();

    static final int COL_ITEM_ID = 0;
    static final int COL_ITEM_TYPE = 1;
    static final int COL_IMG_PATH = 2;
    static final int COL_BRAND = 3;
    static final int COL_CONDITION = 4;
    static final int COL_COLOR = 5;
    static final int COL_MATERIAL = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_new_item) != null) {
            mTwoPane = true;
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (savedInstanceState == null) {
                NewItemFragment fragment = NewItemFragment.newInstance(NEW_ITEM, 0, true);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_new_item, fragment, DETAILFRAGMENT_TAG)
                            .commit();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }

        ItemFragment itemFragment =  ((ItemFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_item));
        getSupportActionBar().setElevation(0f);
        itemFragment.setUseGridLayout(true);
        mGridView = (GridView) findViewById(R.id.gridview_item);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(clickListener);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //items selected should not be greater than 9. (maximum capable collage images)
            if (mGridView.getCheckedItemIds().length <= 0 || mGridView.getCheckedItemIds().length > 9) {
                //return error
                Crouton.makeText(MainActivity.this, "Please select 1 to 9 items only.", Style.ALERT).show();
            }
            else {
                //get the values selected
                ItemTask it = new ItemTask(MainActivity.this);
                Collage collage = COLLAGE_FACTORY.getCollage(mGridView.getCheckedItemIds().length - 1);
                mCollageFillData = new CollageFillData(collage);
                Cursor cursor = it.getItems(mGridView.getCheckedItemIds());
                cursor.moveToPosition(-1);
                int i = 0;
                while (cursor.moveToNext()) {
                    CollageRegion collageRegion = collage.getCollageRegionAtIndex(i);
                    File outputFile = new File(cursor.getString(COL_IMG_PATH));
                    CollageRegionData collageRegionData = new CollageRegionData(outputFile);
                    mCollageFillData.setRegionData(collageRegion, collageRegionData);
                    i++;
                }
                cursor.close();

                if (mCollageFillData.hasAllRegions()) {
                    startActivity(new Intent(MainActivity.this, CollageBuilderActivity.class)
                            .putExtra(CollageBuilderActivity.EXTRA_KOLAJ, mCollageFillData));
                }
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_new:

                if (mTwoPane) {
                    //dispatch camera
                    if (PhotoUtility.isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
                        dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                    } else {
                        NewItemFragment fragment = NewItemFragment.newInstance(NEW_ITEM, 0, true);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_new_item, fragment, DETAILFRAGMENT_TAG)
                                .commit();
                    }
                } else {
                    Intent intent = new Intent(this, NewItemActivity.class);
                    intent.setAction(Intent.ACTION_INSERT);
                    intent.putExtra("mode", NEW_ITEM);
                    intent.putExtra("id", 0);
                    startActivityForResult(new Intent(this, NewItemActivity.class), NEW_ITEM);
                    return true;
                }
                break;
            case R.id.menu_picture :
                if (mTwoPane) {
                    //call gallery
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
                } else {
                    Intent intent = new Intent(this, NewItemActivity.class);
                    intent.setAction(Intent.ACTION_INSERT);
                    intent.putExtra("mode", NEW_ITEM);
                    intent.putExtra("id", (long)0);
                    intent.putExtra("from", FROM_GALLERY);
                    startActivityForResult(intent, NEW_ITEM);
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri dateUri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ItemFragment ff = (ItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_item);
        if ( null != ff ) {
            ff.onResume();
        }
        NewItemFragment df = (NewItemFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
        if ( null != df ) {
            df.onItemEdit();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_ITEM && resultCode == SUCCESS)
        {
            // new item success
            Crouton.makeText(this, "New Item Added", Style.INFO).show();
        }
        else if (requestCode == EDIT_ITEM && resultCode == SUCCESS) {
            // edit item success
            Crouton.makeText(this, "Item Edited", Style.INFO).show();
        }
        else if (mTwoPane){
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                // set image bitmap to new item fragment
                NewItemFragment df = (NewItemFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                if ( null != df ) {
                    df.setImageBitmap(imageBitmap);
                }
            }
            else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
                // TODO: check if multiple images were selected
                Uri uri = data.getData();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    NewItemFragment df = (NewItemFragment)getSupportFragmentManager().findFragmentByTag(DETAILFRAGMENT_TAG);
                    if ( null != df ) {
                        df.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void deleteSelectedItem(long selectedId) {
        ItemTask iit = new ItemTask(this);
        long deletedRows = iit.deleteItem(selectedId);
        if (deletedRows > 0){
            Crouton.makeText(this, "Item Deleted", Style.INFO).show();
        }
    }


    public void editSelectedItem(long selectedId) {
        if (mTwoPane) {
            NewItemFragment fragment = NewItemFragment.newInstance(EDIT_ITEM, selectedId, true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_new_item, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, NewItemActivity.class);
            intent.setAction(Intent.ACTION_EDIT);
            intent.putExtra("mode", EDIT_ITEM);
            intent.putExtra("id", selectedId);
            startActivityForResult(intent, EDIT_ITEM);
        }
    }

}
