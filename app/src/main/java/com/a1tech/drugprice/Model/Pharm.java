package com.a1tech.drugprice.Model;

public class Pharm {

    private final String pharmName;
    private final String thisDrugPrice;
    private final String drugImage;
    private final double lat;
    private final double lon;
    private final String distance;

    public Pharm(String pharmName, String thisDrugPrice, String drugImage, double lat, double lon, String distance) {
        this.pharmName = pharmName;
        this.thisDrugPrice = thisDrugPrice;
        this.drugImage = drugImage;
        this.lat = lat;
        this.lon = lon;
        this.distance = distance;
    }

    public String getPharmName() {
        return pharmName;
    }

    public String getThisDrugPrice() {
        return thisDrugPrice;
    }

    public String getDrugImage() {
        return drugImage;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getDistance() {
        return distance;
    }
}
