package com.redmadrobot.azoft.collage.utils.collagegenerators;

import android.util.SparseArray;

import com.redmadrobot.azoft.collage.data.Collage;
import com.redmadrobot.azoft.collage.utils.CollageRegion;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator with hardcoded numbers of collagees.
 * <p/>
 * Date: 4/8/2014
 * Time: 4:20 PM
 *
 * @author MiG35
 *
 * @author vickysy edited to support up to 9 images
 */
public final class SimpleCollageGenerator implements CollageFactory {

	private static final int KOLAJ_ITEMS_COUNT = 9;
	private final SparseArray<Collage> mCollages = new SparseArray<Collage>();

	@Override
	public Collage getCollage(final int number) {
		if (number < 0 || number >= 9) {
			throw new IllegalArgumentException("SimpleCollageGenerator can create collages from 0 to 9 items only");
		}
		Collage collage = mCollages.get(number);
		if (null == collage) {
			collage = generateCollage(number);
			mCollages.put(number, collage);
		}
		return collage;
	}

	@Override
	public int getCollageCount() {
		return KOLAJ_ITEMS_COUNT;
	}

	@SuppressWarnings({"ValueOfIncrementOrDecrementUsed", "MagicNumber"})
	private Collage generateCollage(final int number) {
		final List<CollageRegion> collageRegions = new ArrayList<CollageRegion>();
		int regionId = 0;
        if (0 == number) {
			collageRegions.add(new CollageRegion(regionId, 0.01, 0.01, 0.99, 0.99));
		}
		else if (1 == number) {
			collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.99));
			collageRegions.add(new CollageRegion(regionId, 0.51d, 0.01, 0.99, 0.99));
		}
		else if (2 == number) {
			collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.99));
			collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.01, 0.99, 0.49d));
			collageRegions.add(new CollageRegion(regionId, 0.51d, 0.51d, 0.99, 0.99));
		}
		else if (3 == number) {
			collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.49d));
			collageRegions.add(new CollageRegion(regionId++, 0.01, 0.51d, 0.49d, 0.99));
			collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.01, 0.99, 0.49d));
			collageRegions.add(new CollageRegion(regionId, 0.51d, 0.51d, 0.99, 0.99));
		}
        else if (4 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.49d, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.33d, 0.49d, 0.64));
            collageRegions.add(new CollageRegion(regionId++, 0.01d, 0.66d, 0.49d, 0.99d));
            collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.01, 0.99, 0.49d));
            collageRegions.add(new CollageRegion(regionId, 0.51d, 0.51d, 0.99, 0.99));
        }
        else if (5 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.31d, 0.49d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.51d, 0.31d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.01d, 0.64d, 0.49));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.51d, 0.64d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.66d, 0.01, 0.99, 0.49d));
            collageRegions.add(new CollageRegion(regionId, 0.66d, 0.51d, 0.99, 0.99));
        }
        else if (6 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.31d, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.33d, 0.49d, 0.64));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.66d, 0.49d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.01d, 0.64d, 0.31));
            collageRegions.add(new CollageRegion(regionId++, 0.66d, 0.01, 0.99, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.33d, 0.99, 0.64));
            collageRegions.add(new CollageRegion(regionId, 0.51d, 0.66d, 0.99, 0.99));
        }
        else if (7 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.31d, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.33d, 0.49d, 0.64));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.66d, 0.31d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.01d, 0.64d, 0.31));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.66d, 0.64d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.66d, 0.01, 0.99, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.51d, 0.33d, 0.99, 0.64));
            collageRegions.add(new CollageRegion(regionId, 0.66d, 0.66d, 0.99, 0.99));
        }
        else if (8 == number) {
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.01, 0.31d, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.33d, 0.31d, 0.64));
            collageRegions.add(new CollageRegion(regionId++, 0.01, 0.66d, 0.31d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.01d, 0.64d, 0.31));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.33d, 0.64d, 0.64));
            collageRegions.add(new CollageRegion(regionId++, 0.33, 0.66d, 0.64d, 0.99));
            collageRegions.add(new CollageRegion(regionId++, 0.66d, 0.01, 0.99, 0.31d));
            collageRegions.add(new CollageRegion(regionId++, 0.66d, 0.33d, 0.99, 0.64));
            collageRegions.add(new CollageRegion(regionId, 0.66d, 0.66d, 0.99, 0.99));
        }
		return new Collage(collageRegions);
	}
}