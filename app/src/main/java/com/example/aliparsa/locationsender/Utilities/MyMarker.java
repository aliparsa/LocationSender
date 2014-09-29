package com.example.aliparsa.locationsender.Utilities;

import android.util.Log;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.overlay.Marker;

/**
 * Created by aliparsa on 9/28/2014.
 */
public class MyMarker extends Marker {
    public MyMarker(LatLong latLong, Bitmap bitmap, int horizontalOffset, int verticalOffset) {
        super(latLong, bitmap, horizontalOffset, verticalOffset);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        //return super.onTap(tapLatLong, layerXY, tapXY);
        Log.e("ali"," lan > "+tapLatLong.latitude) ;
        return false;
    }


}
