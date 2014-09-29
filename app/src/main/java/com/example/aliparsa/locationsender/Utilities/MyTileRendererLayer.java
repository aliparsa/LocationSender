package com.example.aliparsa.locationsender.Utilities;

import android.util.Log;
import android.view.View;

import com.example.aliparsa.locationsender.Interfaces.OnTapListener;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;

/**
 * Created by aliparsa on 9/29/2014.
 */
public class MyTileRendererLayer extends TileRendererLayer {

    OnTapListener listener;

    public MyTileRendererLayer(TileCache tileCache, MapViewPosition mapViewPosition, boolean isTransparent, boolean renderLabels, GraphicFactory graphicFactory) {
        super(tileCache, mapViewPosition, isTransparent, renderLabels, graphicFactory);
    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {


        this.listener.onTap(tapLatLong);
        return super.onTap(tapLatLong, layerXY, tapXY);
    }

    public void setOnTapListener(OnTapListener listener){
        this.listener = listener;
    }
}
