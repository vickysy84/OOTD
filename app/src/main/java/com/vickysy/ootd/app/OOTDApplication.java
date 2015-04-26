package com.vickysy.ootd.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * @author vickysy
 */
public class OOTDApplication extends Application {

	private static OOTDApplication sInstance;

	private Toast mOldToast;
	private Handler mHandler;

	@Override
	public void onCreate() {
		super.onCreate();

		sInstance = this;
		mHandler = new Handler();

		final Picasso picasso = Picasso.with(this);
		picasso.setDebugging(false);
	}

	public static OOTDApplication getInstance() {
		return sInstance;
	}

	/**
	 * Show toast. Previous toast will be canceled.
	 * Thread save.
	 *
	 * @param message data to show for toast
	 * @param gravity if set, this gravity will be used for toast
	 */
	public void showToast(final String message, final Integer gravity) {
		if (getMainLooper() == Looper.myLooper()) {
			if (null != mOldToast) {
				mOldToast.cancel();
			}
			mOldToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
			if (null != gravity) {
				mOldToast.setGravity(gravity, 0, 0);
			}
			mOldToast.show();
		}
		else {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					showToast(message, gravity);
				}
			});
		}
	}
}