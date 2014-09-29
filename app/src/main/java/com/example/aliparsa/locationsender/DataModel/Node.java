package com.example.aliparsa.locationsender.DataModel;

import org.xml.sax.Parser;
import org.xmlpull.v1.XmlPullParser;

/**
 * Created by aliparsa on 9/27/2014.
 */
public class Node {
    public String id;
    public String timestamp;
    public String uid;
    public String user;
    public String visible;
    public String version;
    public String changeset;
    public String lat;
    public String lon;

    public Node(String id, String timestamp, String uid, String user, String visible, String version, String changeset, String lat, String lon) {
        this.id = id;
        this.timestamp = timestamp;
        this.uid = uid;
        this.user = user;
        this.visible = visible;
        this.version = version;
        this.changeset = changeset;
        this.lat = lat;
        this.lon = lon;
    }

    public Node() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getChangeset() {
        return changeset;
    }

    public void setChangeset(String changeset) {
        this.changeset = changeset;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public static Node createNodeFromParser(XmlPullParser parser){

        Node currentNode = new Node();
        currentNode.id = parser.getAttributeValue(null,"id");
//        currentNode.timestamp = parser.getAttributeValue(null,"timestamp");
//        currentNode.uid = parser.getAttributeValue(null,"uid");
//        currentNode.user = parser.getAttributeValue(null,"user");
//        currentNode.visible = parser.getAttributeValue(null,"visible");
//        currentNode.version = parser.getAttributeValue(null,"version");
 //       currentNode.changeset = parser.getAttributeValue(null,"changeset");
        currentNode.lat = parser.getAttributeValue(null,"lat");
        currentNode.lon = parser.getAttributeValue(null,"lon");
        return currentNode;
    }
}
