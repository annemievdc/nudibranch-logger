package be.thomasmore.nudibranch.util;

import be.thomasmore.nudibranch.model.Location;

public class DataPoint {
    private Double lat;
    private Double lon;
    private int z;
    private String tooltip;



    public DataPoint(Location loc, int z, String tooltip) {
        this.lat = loc.getLatitude();
        this.lon = loc.getLongitude();
        this.z = z; //amount of nudis
        this.tooltip= tooltip;


    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
