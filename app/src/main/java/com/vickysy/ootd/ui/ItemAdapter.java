package com.vickysy.ootd.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vickysy.ootd.R;

/**
 * Created by vickysy on 3/10/15.
 */
public class ItemAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_GRID = 0;
    private static final int VIEW_TYPE_LIST = 1;

    // Flag to determine if we want to use a separate view for "grid".
    private boolean mUseGridLayout = true;
    private LayoutInflater mInflater;

    /**
     * Cache of the children views for a list item.
     */
    public static class ViewHolder {
        public final TextView itemTypeView;
        public final ImageView imagePathView;

        public ViewHolder(View view) {
            itemTypeView = (TextView) view.findViewById(R.id.list_item_type_view);
            imagePathView = (ImageView) view.findViewById(R.id.thumbnailImageView);
        }
    }

    public ItemAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        switch (viewType) {
            case VIEW_TYPE_GRID: {
                layoutId = R.layout.list_item_grid;
                break;
            }
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        view.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.item_view));
        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_GRID: {

                break;
            }
        }

        String itemType = cursor.getString(ItemFragment.COL_ITEM_TYPE);
        viewHolder.itemTypeView.setText(itemType);

        // Read image path from cursor
        String imgPath = cursor.getString(ItemFragment.COL_IMG_PATH);
        if (imgPath != null && !imgPath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            viewHolder.imagePathView.setImageBitmap(bitmap);
        }
    }

    public void setUseGridLayout(boolean useGridLayout){

        mUseGridLayout = useGridLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_GRID;
    }

    @Override
    public int getViewTypeCount() {

        return VIEW_TYPE_COUNT;
    }



}