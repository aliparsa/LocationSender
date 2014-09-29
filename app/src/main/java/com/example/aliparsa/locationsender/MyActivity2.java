package com.example.aliparsa.locationsender;

import android.app.Activity;


import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.aliparsa.locationsender.DataModel.myWay;
import com.example.aliparsa.locationsender.Interfaces.OnTapListener;
import com.example.aliparsa.locationsender.R;
import com.example.aliparsa.locationsender.Utilities.DatabaseHandler;
import com.example.aliparsa.locationsender.Utilities.MyMarker;
import com.example.aliparsa.locationsender.Utilities.MyTileRendererLayer;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.graphics.AndroidResourceBitmap;
import org.mapsforge.map.android.input.MapZoomControls;
import org.mapsforge.map.android.layer.MyLocationOverlay;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Circle;
import org.mapsforge.map.layer.overlay.FixedPixelCircle;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.overlay.Polygon;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyActivity2 extends Activity {
    // name of the map file in the external storage
    private static final String MAPFILE = "iran.map";

    private MapView mapView;
    private TileCache tileCache;
    private MyTileRendererLayer tileRendererLayer;
    LocationManager locationManager;
    LatLong lastKnowLocation;

    MyActivity2 context;
    private boolean shouldFollowGPSLocation=false;
    private LatLong lastUserLocationOnMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidGraphicFactory.createInstance(this.getApplication());

        context = this;
        this.mapView = new MapView(this);
        setContentView(mapView);

        this.mapView.setClickable(true);
        this.mapView.getMapScaleBar().setVisible(true);
        this.mapView.setBuiltInZoomControls(true);

        this.mapView.getMapZoomControls().setZoomLevelMin((byte) 10);
        this.mapView.getMapZoomControls().setZoomLevelMax((byte) 20);





        // create a tile cache of suitable size
        this.tileCache = AndroidUtil.createTileCache(this, "mapcache",
                mapView.getModel().displayModel.getTileSize(), 1f,
                this.mapView.getModel().frameBufferModel.getOverdrawFactor());




    }
    @Override
    protected void onStart() {
        super.onStart();

    // tile renderer layer using internal render theme
        this.tileRendererLayer = new MyTileRendererLayer(
                tileCache,
                this.mapView.getModel().mapViewPosition,
                false,
                true,
                AndroidGraphicFactory.INSTANCE);

        tileRendererLayer.setMapFile(getMapFile());
        tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.OSMARENDER);



        tileRendererLayer.setOnTapListener(new OnTapListener() {
            @Override
            public void onTap(LatLong latLong) {

                DatabaseHandler db = new DatabaseHandler(context);
                ArrayList<LatLong> latLongs = db.getAroundPoints(latLong,50);
                drawPoint(latLongs);
                Log.i("ali","finish");
            }
        });

     /*  try {
            XmlRenderTheme xmlRenderTheme = new AssetsRenderTheme(this, "", "myRender.xml");
            tileRendererLayer.setXmlRenderTheme(xmlRenderTheme);
        }catch (Exception e){
            e.printStackTrace();
        }*/


        //mapView.getModel().displayModel. ( android.graphics.Color.parseColor("#c8bdb9"));




        this.mapView.getModel().mapViewPosition.setCenter(getLastKnownLocation());
        this.mapView.getModel().mapViewPosition.setZoomLevel((byte) 15);

        // only once a layer is associated with a mapView the rendering starts
        emptyMapViewLayers(mapView);
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);



        //showIMGOverlayOnThisPoint(lastKnowLocation,R.drawable.point);

        LocationManager locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 5, listener);


       // showLastKnownLocation();
        //showAllLocations();

    }
    private void drawPoint(ArrayList<LatLong> latLongs) {
        for (int i = 0; i < latLongs.size(); i++) {

            MyMarker marker = new MyMarker(latLongs.get(i),
                    AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.point)) ,
                    0,
                    0);

            mapView.getLayerManager().getLayers().add(marker);

        }

    }
    private void drawPoint(LatLong latLong) {


            MyMarker marker = new MyMarker(latLong,
                    AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(R.drawable.point)) ,
                    0,
                    0);

            mapView.getLayerManager().getLayers().add(marker);

        }
    private void emptyMapViewLayers(MapView mapView) {
        while(mapView.getLayerManager().getLayers().size()>0)
        {
            mapView.getLayerManager().getLayers().remove(0);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_activity2, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        this.mapView.getLayerManager().getLayers().remove(this.tileRendererLayer);
        this.tileRendererLayer.onDestroy();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.tileCache.destroy();
        this.mapView.getModel().mapViewPosition.destroy();
        this.mapView.destroy();
        AndroidResourceBitmap.clearResourceBitmaps();
    }
    private File getMapFile() {
        File file = new File(Environment.getExternalStorageDirectory(), MAPFILE);
        return file;
    }
    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            lastKnowLocation =  new LatLong(location.getLatitude(),location.getLongitude());
            showIMGOverlayOnThisPoint(lastKnowLocation,R.drawable.point);

            if (true)
                animateToPoint(location.getLatitude(),location.getLongitude());
            //showThisPointOnMap(location.getLatitude(),location.getLongitude());

            //Toast.makeText(context,"new location :)",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(context,"onProviderDisabled",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            //Toast.makeText(context,"onProviderEnabled",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            //Toast.makeText(context,"onStatusChanged",Toast.LENGTH_SHORT).show();


        }
    };
    private void animateToPoint(double lat, double lon){
        LatLong latlong = new LatLong(lat,lon);
        mapView.getModel().mapViewPosition.animateTo(latlong);
    }
    private void showAllLocations(){
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<LatLong> latlong_list= db.getAllLocations();

        for (int i = 0; i < latlong_list.size(); i++) {
            showIMGOverlayOnThisPoint(latlong_list.get(i),R.drawable.point);

        }

    }
    private LatLong getLastKnownLocation(){

        if(lastUserLocationOnMap!=null)
            return lastUserLocationOnMap;

        DatabaseHandler db = new DatabaseHandler(this);
        LatLong lastLatLong = db.getLastKnownLocation();

        if (lastLatLong==null) {
            double defaultLat = 29.6119;
            double defaultLon = 52.5307;
            lastLatLong = new LatLong(defaultLat, defaultLon);
        }
        return lastLatLong;
    }
    @Override
    protected void onPause() {
        super.onPause();
        lastUserLocationOnMap =  this.mapView.getModel().mapViewPosition.getCenter();
    }
    private void followGPSLocation(boolean state){
        this.shouldFollowGPSLocation= state;
    }
    private void drawFixedCircle(LatLong latlon){
        // instantiating the paint object
        Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();
        paint.setColor(10);
        paint.setStrokeWidth(1);
        paint.setStyle(Style.FILL);

// instantiating the polyline object
        //Polyline polyline = new Polyline(paint, AndroidGraphicFactory.INSTANCE);
        //Polygon polygon = new Polygon(paint,paint,AndroidGraphicFactory.INSTANCE);

        FixedPixelCircle fixedPixelCircle = new FixedPixelCircle(latlon,20,paint,paint);

// adding the layer to the mapview
        mapView.getLayerManager().getLayers().add(fixedPixelCircle);
    }
    private void showIMGOverlayOnThisPoint(LatLong latlong,int IMGresID) {
        MyMarker marker = new MyMarker(latlong,
                AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(IMGresID)) ,
                0,
                0);



        while(mapView.getLayerManager().getLayers().size()>1)
        {
            mapView.getLayerManager().getLayers().remove(1);
        }
        mapView.getLayerManager().getLayers().add(marker);

    }

}
