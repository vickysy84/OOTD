package com.redmadrobot.azoft.collage.ui.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.exceptions.CollageCreationException;
import com.redmadrobot.azoft.collage.loaders.CollagePreviewCreatorLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vickysy.ootd.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Date: 4/9/2014
 * Time: 5:57 PM
 *
 * @author MiG35
 *
 * 5/2/2015
 * Note: changed implementation to ShareActionProvider to turn off history.
 * @author vickysy
 */
public class CollagePreviewFragment extends ActionBarLoaderFragment {

    private static final String KOLAJ_PREVIEW_LOADER = "com.redmadrobot.azoft.collage.ui.fragments.CollagePreviewFragment.KOLAJ_PREVIEW_LOADER";
    private static final String CREATION_PROBLEM_FRAGMENT =
            "com.redmadrobot.azoft.collage.ui.fragments.CollagePreviewFragment.CREATION_PROBLEM_FRAGMENT";

    @InjectSavedState
    private CollageFillData mCollageFillData;

    @InjectView(R.id.iv_collage)
    private ImageView mCollageImageView;

    @InjectSavedState
    private String mResultPath;


    public CollagePreviewFragment() {
        setHasOptionsMenu(true);
        setMenuVisibility(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_collage_preview, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // we should check if there is no big errors in previous image creation
        if (null == getFragmentManager().findFragmentByTag(CREATION_PROBLEM_FRAGMENT)) {

            // we should check if we already create image or not
            if (null == mResultPath) {
                getLoaderHelper().initAsyncLoader(getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER),
                        new CollagePreviewCreatorLoader(getActivity(), mCollageFillData));
            }
            else {
                setResultPath(mResultPath);
            }
        }
    }

    @Override
    public void onLoaderResult(final int id, final Object result) {
        super.onLoaderResult(id, result);

        if (id == getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER)) {
            getLoaderHelper().removeLoaderFromRunningLoaders(id);

            setResultPath((String) result);
        }
    }

    @Override
    public void onLoaderError(final int id, final Exception exception) {
        super.onLoaderError(id, exception);

        if (id == getLoaderHelper().getLoaderId(KOLAJ_PREVIEW_LOADER) && exception instanceof CollageCreationException) {
            final FragmentManager fm = getFragmentManager();
            fm.beginTransaction().add(new CollageCreationProblemMessageFragment(), CREATION_PROBLEM_FRAGMENT).commitAllowingStateLoss();
        }
    }

    private void setResultPath(final String resultPath) {
        mResultPath = resultPath;

        Picasso.with(getActivity()).load(resultPath).fit().error(R.drawable.ic_action_new).
                skipMemoryCache().into(mCollageImageView, new Callback() {
            @Override
            public void onSuccess() {
                // Setup share intent now that image has loaded
                setupShareIntent();
            }

            @Override
            public void onError() {
            }
        });


        getActionBarActivity().supportInvalidateOptionsMenu();
    }

    private final String[] INTENT_FILTER = new String[] {
            "com.twitter.android",
            "com.facebook.katana"
    };

    ShareActionProvider mShareActionProvider;

    @Override
    public void onCreateOptionsMenu(Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.collage_preview, menu);

        // Set up ShareActionProvider's default share intent
        MenuItem shareItem = menu.findItem(R.id.menu_share_collage);

        mShareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(shareItem);
        if (mResultPath != null) {
            ((SupportMenuItem) shareItem).setSupportActionProvider(mShareActionProvider);
        }

    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file =  new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void setupShareIntent() {
        // Fetch Bitmap Uri locally

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        ImageView ivImage = (ImageView) getActivity().findViewById(R.id.iv_collage);
        Uri bmpUri = getLocalBitmapUri(ivImage);
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        // Attach share event to the menu item provider
        mShareActionProvider.setShareIntent(shareIntent);
    }

    public static Fragment getInstance(final CollageFillData collageFillData) {
        if (null == collageFillData) {
            throw new IllegalArgumentException("collageFillData can't be null");
        }
        final CollagePreviewFragment collagePreviewFragment = new CollagePreviewFragment();
        collagePreviewFragment.mCollageFillData = collageFillData;
        return collagePreviewFragment;
    }

    public static class CollageCreationProblemMessageFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_collage_creation_problem_title);
            builder.setMessage(R.string.dialog_collage_creation_problem_message);
            builder.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    getActivity().finish();
                }
            });

            return builder.create();
        }

        @Override
        public void onCancel(final DialogInterface dialog) {
            super.onCancel(dialog);

            getActivity().finish();
        }
    }
}