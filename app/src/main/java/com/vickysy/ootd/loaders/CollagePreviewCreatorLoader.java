package com.vickysy.ootd.loaders;

import android.content.Context;

import com.mig35.loaderlib.loaders.DataAsyncTaskLibLoader;

/**
 * Created by vickysy on 3/21/15.
 */
public class CollagePreviewCreatorLoader extends DataAsyncTaskLibLoader<String> {

    //private final CollageFillData mCollageFillData;

    public CollagePreviewCreatorLoader(final Context context/*, final CollageFillData collageFillData*/) {
        super(context);
//        if (null == collageFillData) {
//            throw new IllegalArgumentException("collageFillData can't be null");
//        }
//
//        mCollageFillData = collageFillData;
    }
    @Override
    protected String performLoad() throws Exception {
        return null;
    }
}
