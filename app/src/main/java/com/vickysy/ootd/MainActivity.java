package com.vickysy.ootd;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


public class MainActivity extends ActionBarActivity implements ItemFragment.Callback{

    private static final int NEW_ITEM = 0;
    private static final int EDIT_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);
        ItemFragment itemFragment =  ((ItemFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_item));
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
                Intent intent = new Intent(this, NewItemActivity.class);
                intent.setAction(Intent.ACTION_INSERT);
                intent.putExtra("mode", NEW_ITEM);
                intent.putExtra("id", 0);
                startActivityForResult(new Intent(this, NewItemActivity.class), NEW_ITEM);
                return true;

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
            // new item success

            //this.sendBroadcast(data);
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
