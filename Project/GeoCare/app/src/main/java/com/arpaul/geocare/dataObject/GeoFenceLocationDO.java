package com.arpaul.geocare.dataObject;

/**
 * Created by Aritra on 6/15/2016.
 */
public class GeoFenceLocationDO extends BaseDO {
    public int LocationId               = 0;
    public String LocationName          = "";
    public String Address               = "";
    public double Latitude              = 0.0;
    public double Longitude             = 0.0;

    public static final String LOCATIONID       = "LOCATIONID";
    public static final String LOCATIONNAME     = "LOCATIONNAME";
    public static final String ADDRESS          = "ADDRESS";
    public static final String LATITUDE         = "LATITUDE";
    public static final String LONGITUDE        = "LONGITUDE";
    public static final String EVENT            = "EVENT";
    public static final String OCCURANCETIME    = "OCCURANCETIME";
}
