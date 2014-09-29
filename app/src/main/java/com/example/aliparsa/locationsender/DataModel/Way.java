package com.example.aliparsa.locationsender.DataModel;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by aliparsa on 9/27/2014.
 */
public class Way {
    private int id;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Way createWayFromParser(XmlPullParser parser) {
        Way way = new Way();
        way.id = Integer.parseInt(parser.getAttributeValue(null,"id"));
        return way;
    }

}
