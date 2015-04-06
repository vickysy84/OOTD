package com.vickysy.ootd.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.vickysy.ootd.R;
import com.vickysy.ootd.utils.camera.PhotoUtility;


public class NewItemActivity extends ActionBarActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static final int REQUEST_IMAGE_CAPTURE = 2;

    private long id = 0;
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras2 = intent.getExtras();

        if (extras2 != null) {
            mode = extras2.getInt("mode");
            id = extras2.getLong("id");
        }
        if (mode == NewItemFragment.NEW_ITEM){
            setTitle("New Item");
        }
        else {
            setTitle("Edit Item");
        }
        setContentView(R.layout.activity_new_item);

        if (savedInstanceState == null) {

            // check if add
            switch (mode) {
                case NewItemFragment.NEW_ITEM:
                    // call camera intent
                    if (PhotoUtility.isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
                        dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                    } else {
                        //log error
                        NewItemFragment fragment = NewItemFragment.newInstance(mode, id, false);
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.fragment_new_item, fragment)
                                .commit();
                    }
                    break;
                case NewItemFragment.EDIT_ITEM:
                    NewItemFragment fragment = NewItemFragment.newInstance(mode, id, false);
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_new_item, fragment)
                            .commit();
                    break;
            }

        }
    }


    private void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, actionCode);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            NewItemFragment fragment = NewItemFragment.newInstance(mode, id, imageBitmap, false);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_new_item, fragment)
                    .commit();
        } else if ( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            this.finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NewItemFragment df = (NewItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_new_item);
        if (df != null) {
            df.onItemEdit();
        }
    }
}
