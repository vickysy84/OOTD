package com.vickysy.ootd;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.manuelpeinado.multichoiceadapter.MultiChoiceBaseAdapter;

import java.util.List;

/**
 * Created by vickysy on 3/20/15.
 */
public class GalleryAdapter extends MultiChoiceBaseAdapter {
    private List<GalleryImage> buildings;

    public GalleryAdapter(Bundle savedInstanceState, List<GalleryImage> buildings) {
        super(savedInstanceState);
        this.buildings = buildings;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.my_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//        if (item.getItemId() == R.id.menu_share) {
//            Toast.makeText(getContext(), "Share", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public int getCount() {
        return buildings.size();
    }

    @Override
    public GalleryImage getItem(int position) {
        return buildings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected View getViewImpl(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
//            int layout = R.layout.gallery_item;
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(layout, parent, false);
        }
        ImageView imageView = (ImageView) convertView;
        GalleryImage building = getItem(position);
        imageView.setImageDrawable(building.photo);
        return imageView;
    }
}
