package com.example.aliparsa.locationsender;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aliparsa.locationsender.Services.LocationFinderService;
import com.example.aliparsa.locationsender.Services.LocationSenderService;
import com.example.aliparsa.locationsender.Utilities.DatabaseHandler;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;


public class MyActivity extends Activity {
    LocationManager locationManager;
    private boolean isFinderServiceRunning;
    private boolean isSenderServiceRunning;
    public static EditText edittext;

    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_my);


        Intent intent = new Intent(this,MyActivity2.class);
        startActivity(intent);



        hideButtonBar();

        context=this;
        edittext = (EditText) findViewById(R.id.editText);

        //RunLocationFinderService();
        //RunLocationSenderService();

         locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 5, listener);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }

    private void hideButtonBar() {
        final String ACTION_HIDE_SYSTEMBAR = "com.example.aliparsa.locationsender.ACTION_HIDE_SYSTEMBAR";
        sendBroadcast(new Intent(ACTION_HIDE_SYSTEMBAR));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context=null;
    }

    private void RunLocationSenderService() {
        String status = "Not defined";
        if (isSenderServiceRunning)
            status = "running";
        else
            status = "not running";


        Log.i("ali", "Sender Service running status: " + status);
        alog( "Sender Service running status: " + status);


        if(isSenderServiceRunning)
            return;
        else {
            Intent mServiceIntent = new Intent(this, LocationSenderService.class);
            startService(mServiceIntent);
            isSenderServiceRunning = true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

       private void RunLocationFinderService() {
           String status = "Not defined";
           if (isFinderServiceRunning)
               status = "running";
           else
               status = "not running";


           Log.i("ali", "Finder Service running status: " + status);
           alog("Finder Service running status: " + status);


           if (isFinderServiceRunning)
               return;
           else {
               Intent mServiceIntent = new Intent(this, LocationFinderService.class);
               startService(mServiceIntent);
               isFinderServiceRunning = true;
           }

    }

    public static Context getThis(){
        return context;
    }

    public void alog(String str){
        edittext.append(str+"\n");
        edittext.scrollTo(0,999999999);
    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub
            alog("Loc Changed  lat>" + location.getLatitude() + " lon > " + location.getLongitude() + "\n");
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            alog("onProviderDisabled");        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
             alog("onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
            alog("onStatusChanged");

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showAllPointFromDb(){
        DatabaseHandler db = new DatabaseHandler(this);
        ArrayList<LatLong> latlonList = db.getAllLocations();
    }
}
