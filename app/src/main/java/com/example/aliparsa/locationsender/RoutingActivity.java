package com.example.aliparsa.locationsender;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aliparsa.locationsender.DataModel.Node;
import com.example.aliparsa.locationsender.DataModel.Way;
import com.example.aliparsa.locationsender.DataModel.myWay;
import com.example.aliparsa.locationsender.Utilities.DatabaseHandler;

import org.mapsforge.core.model.LatLong;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class RoutingActivity extends Activity {

    String DB_PATH = "/data/data/com.example.aliparsa.locationsender/databases/";
    String DB_NAME ="db.db";// Database name

    RoutingActivity self;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);
        self = this;

        try {


            createDataBase();


        }
        catch (Exception e){
            e.printStackTrace();
        }


        DatabaseHandler db = new DatabaseHandler(this);




        LatLong start = new LatLong(29.62719559,52.5126170);

        ArrayList<myWay> way_list = db.getNearestStreetToPoint(start);

    //    LatLong rnf = new LatLong(29.6130177,52.5437089);

        myWay  bestWay = null;
        double minDistance = Double.MAX_VALUE;
        myWay lastmyWay = null;

        for (int i = 0; i < way_list.size(); i++) {

            myWay myway = way_list.get(i);


            if ((lastmyWay==null )){
                lastmyWay=myway;
                continue;
            }

            if ((myway.way_id!=lastmyWay.way_id)) {
                lastmyWay=myway;
                continue;
            }


            double distance = pointToLineDistance(new LatLong(lastmyWay.lat,lastmyWay.lon),new LatLong(myway.lat,myway.lon),start);

            if (distance<minDistance)
            {
                minDistance=distance;
                bestWay=myway;
            }

            lastmyWay = myway;

        }

        int x = 10;

     //   db.db.emptyNodesTable();

       /* XmlPullParserFactory pullParserFactory;

        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("mymap.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);


        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ArrayList<Node> nodes =  db.db.getAllNodes();
        Log.i("ali","all node read");*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.routing, menu);
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


    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {

        int eventType = parser.getEventType();
        DatabaseHandler db = new DatabaseHandler(self);
        Way way=null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){

                case XmlPullParser.START_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equals("node")) {
                            db.insertNode(Node.createNodeFromParser(parser));
                    }


                     if (name.equals("way")) {
                         way = Way.createWayFromParser(parser);
                         db.insertWay(way);
                    }

                    if (name.equals("nd") && way!=null) {
                        db.insertWayNodes(way.getId(),Integer.parseInt(parser.getAttributeValue(null,"id")));
                    }

                    break;

                case XmlPullParser.END_TAG:
                    name = parser.getName();

                    if (name.equalsIgnoreCase("way") ){
                        way=null;

                    }
            }
            eventType = parser.next();
        }

        //printProducts(products);
    }

    public void createDataBase() throws IOException
    {
        //If database not exists copy it from the assets

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.e("ali", "createDatabase database created");
            }
            catch (IOException mIOException)
            {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException
    {
        InputStream mInput = getApplicationContext().getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public double pointToLineDistance(LatLong A, LatLong B, LatLong P) {
        double normalLength = Math.sqrt((B.latitude-A.latitude)*(B.latitude-A.latitude)+(B.longitude-A.longitude)*(B.longitude-A.longitude));
        return Math.abs((P.latitude-A.latitude)*(B.longitude-A.longitude)-(P.longitude-A.longitude)*(B.latitude-A.latitude))/normalLength;
    }
}
