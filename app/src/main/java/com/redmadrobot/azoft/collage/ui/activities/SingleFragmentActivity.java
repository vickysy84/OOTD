package com.redmadrobot.azoft.collage.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.vickysy.ootd.R;

/**
 * This activity will handle fragment creation and call createFragment when needed.
 * <p/>
 * Date: 4/8/2014
 * Time: 11:19 AM
 *
 * @author MiG35
 */
public abstract class SingleFragmentActivity extends CollageActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_single_fragment);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		final FragmentManager fm = getSupportFragmentManager();
		if (null == fm.findFragmentById(R.id.container)) {
			final Fragment fragment = createFragment();
			if (null == fragment) {
				finish();
			}
			else {
				fm.beginTransaction().add(R.id.container, fragment).commit();
			}
		}
	}

	protected abstract Fragment createFragment();
}