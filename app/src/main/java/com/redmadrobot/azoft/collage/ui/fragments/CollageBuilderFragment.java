package com.redmadrobot.azoft.collage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mig35.injectorlib.utils.inject.InjectSavedState;
import com.mig35.injectorlib.utils.inject.InjectView;
import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.data.CollageRegionData;
import com.redmadrobot.azoft.collage.ui.widgets.CollageViewGroup;
import com.redmadrobot.azoft.collage.utils.CollageRegion;
import com.vickysy.ootd.R;
import com.redmadrobot.azoft.collage.ui.activities.CollagePreviewActivity;

/**
 * Will show collage and performs all actions for its filling.
 * <p/>
 * Date: 4/8/2014
 * Time: 6:41 PM
 *
 * @author MiG35
 */
public class CollageBuilderFragment extends ActionBarLoaderFragment {

	private static final String BIG_IMAGE_LOADER = "com.redmadrobot.azoft.collage.ui.fragments.CollageBuilderFragment.BIG_IMAGE_LOADER_%d";
	private static final int REQUEST_CODE_IMAGE_SELECTION = 156;

	@InjectSavedState
	private CollageFillData mCollageFillData;
	@InjectSavedState
	private CollageRegion mSelectedCollageRegion;

	@InjectView(R.id.collage_view_group)
	private CollageViewGroup mCollageViewGroup;

	public CollageBuilderFragment() {
		setHasOptionsMenu(true);
		setMenuVisibility(true);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_collage_builder, container, false);
	}

	@Override
	public void onViewCreated(final View view, final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mCollageViewGroup.setCollage(mCollageFillData);
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.collage_builder, menu);
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
        if (R.id.menu_preview_collage == item.getItemId()) {
			if (mCollageFillData.hasAllRegions()) {
				startActivity(new Intent(getActivity(), CollagePreviewActivity.class)
						.putExtra(CollagePreviewActivity.EXTRA_KOLAJ_FILL_DATA, mCollageFillData));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLoaderResult(final int id, final Object result) {
		super.onLoaderResult(id, result);

		for (final CollageRegion collageRegion : mCollageFillData.getCollageRegions()) {
			if (id == getLoaderHelper().getLoaderId(createCollageRegionLoaderId(collageRegion))) {
				getLoaderHelper().removeLoaderFromRunningLoaders(id);

				final CollageRegionData collageRegionData = (CollageRegionData) result;
				mCollageFillData.setRegionData(collageRegion, collageRegionData);
				mCollageViewGroup.invalidateRegionData();

				break;
			}
		}
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (REQUEST_CODE_IMAGE_SELECTION == requestCode) {
			if (resultCode == Activity.RESULT_OK && null != data) {
				if (null == mSelectedCollageRegion) {
					Log.e(CollageBuilderFragment.class.getSimpleName(), "Wrong state! How does it happen?! =/");
					return;
				}
			}
			mSelectedCollageRegion = null;
		}
		else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private String createCollageRegionLoaderId(final CollageRegion collageRegion) {
		return String.format(BIG_IMAGE_LOADER, collageRegion.getId());
	}

	public static Fragment getInstance(final CollageFillData collageFillData) {
		final CollageBuilderFragment collageBuilderFragment = new CollageBuilderFragment();
		collageBuilderFragment.mCollageFillData = collageFillData;
		return collageBuilderFragment;
	}
}