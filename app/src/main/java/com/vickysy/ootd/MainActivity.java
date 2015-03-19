package com.vickysy.ootd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity implements ItemFragment.Callback{

    private static final int NEW_ITEM = 0;
    private static final int EDIT_ITEM = 1;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.fragment_new_item) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_new_item, new NewItemFragment(), DETAILFRAGMENT_TAG)
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
        GridView gridView = (GridView) findViewById(R.id.gridview_item);
        registerForContextMenu(gridView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {

            case R.id.menu_new:
                //
                if (mTwoPane) {
                    // In two-pane mode, show the detail view in this activity by
                    // adding or replacing the detail fragment using a
                    // fragment transaction.
//                    Bundle args = new Bundle();
//                    args.putParcelable(NewItemFragment.ITEM_URI, OOTDContract.ItemEntry.buildItemUri());

                    NewItemFragment fragment = NewItemFragment.newInstance(NEW_ITEM, 0);
                    //fragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_new_item, fragment, DETAILFRAGMENT_TAG)
                            .commit();
                } else {
                    Intent intent = new Intent(this, NewItemActivity.class);
                    intent.setAction(Intent.ACTION_INSERT);
                    intent.putExtra("mode", NEW_ITEM);
                    intent.putExtra("id", 0);
                    startActivityForResult(new Intent(this, NewItemActivity.class), NEW_ITEM);
                    return true;
                }


            case R.id.menu_settings:
                // Here we would open up our settings activity
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri dateUri) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_ITEM)
        {
            Log.i("newitem", "new item");
            // new item success
            Toast toast2 = Toast.makeText(this, "New Item Added", Toast.LENGTH_SHORT);
            toast2.show();
        }
        else if (requestCode == EDIT_ITEM) {
            // new item success
            Toast toast3 = Toast.makeText(this, "Item Edited", Toast.LENGTH_SHORT);
            toast3.show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_long_click, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int id = item.getItemId();
        switch (id) {

            case R.id.menu_edit:
                Intent intent = new Intent(this, NewItemActivity.class);
                intent.setAction(Intent.ACTION_EDIT);
                intent.putExtra("mode", EDIT_ITEM);
                intent.putExtra("id", info.id);
               // intent.setData(OOTDContract.ItemEntry.buildItemUriWithId(info.id));
                startActivityForResult(intent, EDIT_ITEM);
                return true;

            case R.id.menu_delete:
                ItemTask iit = new ItemTask(this);
                long deletedRows = iit.deleteItem(info.id);
                return true;
        }
        return super.onContextItemSelected(item);
    }

}
