package com.a1tech.drugprice.Model;

public class Pharm {

    private final String pharmName;
    private final String thisDrugPrice;
    private final String distance;
    private final String drugImage;
    private final double lat;
    private final double lon;

    public Pharm(String pharmName, String thisDrugPrice, String distance, String drugImage, double lat, double lon) {
        this.pharmName = pharmName;
        this.thisDrugPrice = thisDrugPrice;
        this.distance = distance;
        this.drugImage = drugImage;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPharmName() {
        return pharmName;
    }

    public String getThisDrugPrice() {
        return thisDrugPrice;
    }

    public String getDistance() {
        return distance;
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
}
