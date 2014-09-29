package com.example.aliparsa.locationsender.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.example.aliparsa.locationsender.MyActivity;
import com.example.aliparsa.locationsender.Utilities.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by aliparsa on 9/20/2014.
 */
public class LocationFinderService extends Service {

    PowerManager.WakeLock wakeLock;

    private LocationManager locationManager;

    public LocationFinderService() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        PowerManager pm = (PowerManager) getSystemService(this.POWER_SERVICE);

        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "DoNotSleep");

        // Toast.makeText(getApplicationContext(), "Service Created",
        // Toast.LENGTH_SHORT).show();

        Log.i("ali", "Finder Service Created");
//        ((MyActivity) MyActivity.context).alog("Finder Service Created");

    }


    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);

        Log.i("ali", "Finder Service Started");
//        ((MyActivity) MyActivity.context).alog("Finder Service Started");


        locationManager = (LocationManager) getApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 5, listener);

    }

    private LocationListener listener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // TODO Auto-generated method stub

            Toast.makeText(MyActivity.context,"Location Changed",Toast.LENGTH_SHORT).show();
            Log.i("ali", "Location Changed lat >"+location.getLatitude()+" lon >"+location.getLongitude());

            if (MyActivity.context != null)
            ((MyActivity) MyActivity.context).alog("Location Changed");


            if (location == null)
                return;

            // TODO save Location To DISK
            DatabaseHandler db_handler = new DatabaseHandler(MyActivity.context);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = sdf.format(c.getTime());

            db_handler.insertLocation(location.getLatitude(), location.getLongitude(), strDate, location.getSpeed(), "3");
            Log.i("ali", "Location Inserted to Database");

            if (MyActivity.context != null)
            ((MyActivity) MyActivity.context).alog("Location Inserted to Database");


        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
           // Toast.makeText(MyActivity.context,"onProviderDisabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
           // Toast.makeText(MyActivity.context,"onProviderEnabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }
    };

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        sendBroadcast(new Intent("LOCATION_FINDER"));
        wakeLock.release();


    }

    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }




}
