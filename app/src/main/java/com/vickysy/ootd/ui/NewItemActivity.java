package com.vickysy.ootd.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.vickysy.ootd.R;
import com.vickysy.ootd.utils.camera.PhotoUtility;

import java.io.IOException;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * The New Item activity (home screen) of the OOTD App.
 * Created by vickysy on 3/10/15.
 */
public class NewItemActivity extends ActionBarActivity {
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_IMAGE_GALLERY = 3;

    private long id = 0;
    private int mode = 0;
    private int from = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras2 = intent.getExtras();

        if (extras2 != null) {
            mode = extras2.getInt("mode");
            id = extras2.getLong("id");
            from = extras2.getInt("from");
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
                    switch (from) {
                        // call camera intent
                        case MainActivity.FROM_CAMERA:
                            PackageManager pm = this.getPackageManager();

                            if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                                if (PhotoUtility.isIntentAvailable(this, MediaStore.ACTION_IMAGE_CAPTURE)) {
                                    dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE);
                                } else {
                                    //log error
                                    Crouton.makeText(this, "No camera intent available.", Style.ALERT).show();

                                }
                            } else {
                                //log error
                                Crouton.makeText(this, "No camera available.", Style.ALERT).show();

                            }
                            break;
                        case MainActivity.FROM_GALLERY:
                            Intent intent2 = new Intent();
                            intent2.setType("image/*");
                            // intent2.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //TODO: Allow multiple inserts
                            intent2.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent2, "Select Picture"), REQUEST_IMAGE_GALLERY);
                            break;
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
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            // TODO: check if multiple images were selected
            Uri uri = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            NewItemFragment fragment = NewItemFragment.newInstance(mode, id, bitmap, false);
            getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_new_item, fragment)
                        .commit();
        } else if ( requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_CANCELED) {
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
    protected void onResume() {
        super.onResume();
        NewItemFragment df = (NewItemFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_new_item);
        if (df != null) {
            df.onItemEdit();
        }
    }
}
