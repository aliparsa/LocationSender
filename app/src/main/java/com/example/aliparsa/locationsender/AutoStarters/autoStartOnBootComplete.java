package com.example.aliparsa.locationsender.AutoStarters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.aliparsa.locationsender.MyActivity;
import com.example.aliparsa.locationsender.Services.LocationFinderService;
import com.example.aliparsa.locationsender.Services.LocationSenderService;

/**
 * Created by aliparsa on 9/21/2014.
 */
public class autoStartOnBootComplete extends BroadcastReceiver
{
    private boolean isFinderServiceRunning;

    public void onReceive(Context arg0, Intent arg1)
    {

        Intent mServiceIntent0 = new Intent(arg0, MyActivity.class);
        mServiceIntent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        arg0.startActivity(mServiceIntent0);


        // start location finder on boot complete
        Intent mServiceIntent = new Intent(arg0, LocationFinderService.class);
        arg0.startService(mServiceIntent);

        // start location Sender on boot complete
        Intent mServiceIntent2 = new Intent(arg0, LocationSenderService.class);
        arg0.startService(mServiceIntent2);

    }








}
