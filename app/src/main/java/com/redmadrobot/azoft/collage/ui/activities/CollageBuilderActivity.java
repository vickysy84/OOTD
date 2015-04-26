package com.redmadrobot.azoft.collage.ui.activities;

import android.support.v4.app.Fragment;

import com.redmadrobot.azoft.collage.data.CollageFillData;
import com.redmadrobot.azoft.collage.ui.fragments.CollageBuilderFragment;

import java.io.Serializable;

/**
 * Date: 4/8/2014
 * Time: 6:38 PM
 *
 * @author MiG35
 */
public class CollageBuilderActivity extends SingleFragmentActivity {

	public static final String EXTRA_KOLAJ = "com.redmadrobot.azoft.collage.ui.activities.phone.CollageBuilderActivity.EXTRA_KOLAJ";

	@Override
	protected Fragment createFragment() {
		final Serializable collageFillData = getIntent().getSerializableExtra(EXTRA_KOLAJ);
		if (collageFillData instanceof CollageFillData) {
			return CollageBuilderFragment.getInstance((CollageFillData) collageFillData);
		}
		return null;
	}
}