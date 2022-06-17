package com.a1tech.drugprice.Model;

public class Pharm {

    private final String pharmName;
    private final String thisDrugPrice;
    private final String drugImage;
    private final double lat;
    private final double lon;

    public Pharm(String pharmName, String thisDrugPrice, String drugImage, double lat, double lon) {
        this.pharmName = pharmName;
        this.thisDrugPrice = thisDrugPrice;
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
