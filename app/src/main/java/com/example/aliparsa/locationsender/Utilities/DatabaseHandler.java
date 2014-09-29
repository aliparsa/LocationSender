package com.example.aliparsa.locationsender.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.aliparsa.locationsender.DataModel.MyLocation;
import com.example.aliparsa.locationsender.DataModel.Node;
import com.example.aliparsa.locationsender.DataModel.Way;
import com.example.aliparsa.locationsender.DataModel.myWay;

import org.mapsforge.core.model.LatLong;

import java.util.ArrayList;

/**
 * Created by aliparsa on 9/20/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper {


    // All Static variables
    SQLiteDatabase database;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "db.db";

    // Contacts table name
    private static final String TABLE_LOCATIONS = "locations";
    private static final String TABLE_NODES = "nodes";
    private static final String TABLE_WAYS = "ways";
    private static final String TABLE_WAY_NODES = "way_nodes";


    // Contacts Table Columns names LOCATIONS
    private static final String LOCATIONS_KEY_ID = "id";
    private static final String LOCATIONS_KEY_LAT = "lat";
    private static final String LOCATIONS_KEY_LON = "lon";
    private static final String LOCATIONS_KEY_DATE = "date";
    private static final String LOCATIONS_KEY_DEVICE_ID = "device_id";
    private static final String LOCATIONS_KEY_SPEED = "speed";
    private static final String LOCATIONS_KEY_SENDED = "sended";

    // Contacts Table Columns names NODES
    private static final String NODES_KEY_ID = "id";
    private static final String NODES_KEY_LAT = "lat";
    private static final String NODES_KEY_LON = "lon";

    // Contacts Table Columns names WAYS
    private static final String WAYS_KEY_ID = "id";


    // Contacts Table Columns names WAY_NODES
    private static final String WAY_NODES_KEY_WAY_ID = "way_id";
    private static final String WAY_NODES_KEY_NODE_ID = "node_id";



    private int record_id;
    private int device_id;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        /*String CREATE_CONTACTS_TABLE =
                "CREATE TABLE " + TABLE_LOCATIONS + "("
                        + LOCATIONS_KEY_ID + " INTEGER PRIMARY KEY,"
                        + LOCATIONS_KEY_LAT + " TEXT,"
                        + LOCATIONS_KEY_LON + " TEXT,"
                        + LOCATIONS_KEY_DATE + " TEXT,"
                        + LOCATIONS_KEY_SPEED + " TEXT,"
                        + LOCATIONS_KEY_SENDED + " TEXT,"
                        + LOCATIONS_KEY_DEVICE_ID + " TEXT" + ")";
        db.db.execSQL(CREATE_CONTACTS_TABLE);

        String CREATE_NODES_TABLE =
                "CREATE TABLE " + TABLE_NODES + "("
                        + NODES_KEY_ID + " INTEGER PRIMARY KEY,"
                        + NODES_KEY_LAT + " DOUBLE,"
                        + NODES_KEY_LON + " DOUBLE" + ")";
        db.db.execSQL(CREATE_NODES_TABLE);


        String CREATE_WAYS_TABLE =
                "CREATE TABLE " + TABLE_WAYS + "("
                        + WAYS_KEY_ID + " INTEGER PRIMARY KEY" + ")";
        db.db.execSQL(CREATE_WAYS_TABLE);


        String CREATE_WAY_NODES_TABLE =
                "CREATE TABLE " + TABLE_WAY_NODES + "("
                        + WAY_NODES_KEY_WAY_ID + " INTEGER PRIMARY KEY,"
                        + WAY_NODES_KEY_NODE_ID + " INTEGER" + ")";
        db.db.execSQL(CREATE_WAY_NODES_TABLE);*/

        database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public ArrayList<myWay> getNearestStreetToPoint(LatLong point){

        try {


        SQLiteDatabase db = this.getReadableDatabase();

        final Cursor cursor =  db.rawQuery(
                "select * from way_nodes,nodes where nodes.id=way_nodes.node_id and  way_id in ( "+
                "select way_id from way_nodes where node_id in ( "+
                "select id  FROM nodes ORDER BY ABS("+point.latitude+" - lat) + ABS("+point.longitude+" - lon) ASC limit 5)) order by way_id", null);

        ArrayList<myWay> way_list = new ArrayList<myWay>();
        myWay myway=null;

        if (cursor != null) {
            if(cursor.moveToFirst()) {

                do{
                    myway = new myWay();
                    myway.lat =  cursor.getDouble(cursor.getColumnIndex("lat"));
                    myway.lon =  cursor.getDouble(cursor.getColumnIndex("lon"));
                    myway.node_id =  cursor.getInt(cursor.getColumnIndex("node_id"));
                    myway.way_id =  cursor.getInt(cursor.getColumnIndex("way_id"));
                    myway.seq =  cursor.getInt(cursor.getColumnIndex("seq"));
                    way_list.add(myway);
                }while(cursor.moveToNext());

            }
        }

        return way_list;


        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public void insertLocation(double latitude, double longitude, String date, float speed, String device_id) {
        ContentValues values = new ContentValues();
        values.put(LOCATIONS_KEY_LAT, latitude+"");
        values.put(LOCATIONS_KEY_LON, longitude+"");
        values.put(LOCATIONS_KEY_DATE, date+"");
        values.put(LOCATIONS_KEY_SPEED, speed+"");
        values.put(LOCATIONS_KEY_DEVICE_ID, device_id+"");
        values.put(LOCATIONS_KEY_SENDED, "0");
        this.getWritableDatabase().insert(TABLE_LOCATIONS,null,values);
    }

    public void insertNode(Node node) {
        ContentValues values = new ContentValues();
        values.put(NODES_KEY_ID, node.getId());
   //     values.put(NODES_KEY_TIMESTAMP, node.getTimestamp());
   //     values.put(NODES_KEY_UID, node.getUid());
   //     values.put(NODES_KEY_USER, node.getUser());
   //     values.put(NODES_KEY_VISIBLE,node.getVisible());
   //     values.put(NODES_KEY_CHANGESET, node.getChangeset());
        values.put(NODES_KEY_LAT, node.getLat());
        values.put(NODES_KEY_LON, node.getLon());
        this.getWritableDatabase().insert(TABLE_NODES,null,values);
    }

    public void emptyNodesTable(){
        String strSQL = "Delete from "+TABLE_NODES;
        this.getWritableDatabase().execSQL(strSQL);
        Log.e("ali", "Nodes Table Empty");
    }

/*    public MyLocation getUnsentLocation(){
        final Cursor cursor =  this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_LOCATIONS + " WHERE " + KEY_SENDED + "='0'", null);
        MyLocation location=null;

        if (cursor != null) {
                if(cursor.moveToFirst()) {
                    location = new MyLocation("ali");
                    location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LAT))));
                    location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(KEY_LON))));
                    location.setSpeed(Float.parseFloat(cursor.getString(cursor.getColumnIndex(KEY_SPEED))));
                    location.setRecord_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                    location.setDevice_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID))));
                }
        }
        return location;
    }*/

/*    public void markRecordAsSent(int record_id){

        String strSQL = "UPDATE locations SET sended = '1' WHERE id = "+ record_id;
        this.getWritableDatabase().execSQL(strSQL);
        Log.e("ali", "record sent and mark as sent");
    }*/

    public void bulkMarkRecordsAsSent(int maxID){
        String strSQL = "UPDATE locations SET sended = '1' WHERE id <= "+ maxID;
        this.getWritableDatabase().execSQL(strSQL);
        Log.e("ali", "record sent and mark as sent");
    }

    public ArrayList<MyLocation> getUnsentLocations(){
        final Cursor cursor =  this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_LOCATIONS  + " WHERE " + LOCATIONS_KEY_SENDED + "='0'", null);
        ArrayList<MyLocation> loc_list = new ArrayList<MyLocation>();
        MyLocation location=null;

        if (cursor != null) {
            if(cursor.moveToFirst()) {

                do{
                    location = new MyLocation("ali");
                    location.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LAT))));
                    location.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LON))));
                    location.setSpeed(Float.parseFloat(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_SPEED))));
                    location.setRecord_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_ID))));
                    location.setDevice_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_DEVICE_ID))));
                    location.setDate(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_DATE)));

                    loc_list.add(location);
                }while(cursor.moveToNext());

            }
        }
        return loc_list;
    }

    public ArrayList<LatLong> getAllLocations() {
        final Cursor cursor =  this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_LOCATIONS , null);
        ArrayList<LatLong> latlon_list = new ArrayList<LatLong>();
        LatLong latlong=null;

        if (cursor != null) {
            if(cursor.moveToFirst()) {

                do{
                    latlong = new LatLong(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LAT))),Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LON))));
                    latlon_list.add(latlong);

                }while(cursor.moveToNext());

            }
        }
        return latlon_list;
    }

    public LatLong getLastKnownLocation() {
        final Cursor cursor =  this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_LOCATIONS  , null);
        ArrayList<LatLong> latlon_list = new ArrayList<LatLong>();
        LatLong latlong=null;

        if (cursor != null) {
            if(cursor.moveToLast()) {
                    latlong = new LatLong(Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LAT))),Double.parseDouble(cursor.getString(cursor.getColumnIndex(LOCATIONS_KEY_LON))));
            }
        }

        return latlong;
    }

    public ArrayList<Node> getAllNodes() {
        final Cursor cursor =  this.getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NODES + "WHERE lat<29.6528" , null);
        ArrayList<Node> node_list = new ArrayList<Node>();
        Node node=null;

        if (cursor != null) {
            if(cursor.moveToFirst()) {

                do{
                    node = new Node();
                            node.setId(cursor.getString(cursor.getColumnIndex(NODES_KEY_ID)));
                     //       node.setTimestamp(cursor.getString(cursor.getColumnIndex(NODES_KEY_TIMESTAMP)));
                     //       node.setUid(cursor.getString(cursor.getColumnIndex(NODES_KEY_UID)));
                     //       node.setUser(cursor.getString(cursor.getColumnIndex(NODES_KEY_USER)));
                     //       node.setVisible(cursor.getString(cursor.getColumnIndex(NODES_KEY_VISIBLE)));
                     //       node.setChangeset(cursor.getString(cursor.getColumnIndex(NODES_KEY_CHANGESET)));
                            node.setLat(cursor.getString(cursor.getColumnIndex(NODES_KEY_LAT)));
                            node.setLon(cursor.getString(cursor.getColumnIndex(NODES_KEY_LON)));

                    node_list.add(node);

                }while(cursor.moveToNext());

            }
        }
        return node_list;
    }

    public void insertWay(Way way) {
        ContentValues values = new ContentValues();
        values.put(WAYS_KEY_ID, way.getId());
        this.getWritableDatabase().insert(TABLE_WAYS,null,values);
    }

    public void insertWayNodes(int wayID,int nodeID) {
        ContentValues values = new ContentValues();
        values.put(WAY_NODES_KEY_WAY_ID,wayID);
        values.put(WAY_NODES_KEY_NODE_ID,nodeID);
        this.getWritableDatabase().insert(TABLE_WAY_NODES,null,values);
    }


    // get a "point" and return "countOfPoint" around point
    public ArrayList<LatLong> getAroundPoints(LatLong point,int countOfPoint){
        final Cursor cursor =  this.getReadableDatabase().rawQuery("select *  FROM "+TABLE_NODES+" ORDER BY ABS("+point.latitude+" - lat) + ABS("+point.longitude+" - lon) ASC limit "+countOfPoint , null);
        ArrayList<LatLong> latlon_list = new ArrayList<LatLong>();
        LatLong latlong=null;

        if (cursor != null) {
            if(cursor.moveToFirst()) {

                do{
                    latlong = new LatLong(
                            cursor.getDouble(cursor.getColumnIndex(LOCATIONS_KEY_LAT)),
                            cursor.getDouble(cursor.getColumnIndex(LOCATIONS_KEY_LON)));

                    latlon_list.add(latlong);

                }while(cursor.moveToNext());

            }
        }
        return latlon_list;
    }
}