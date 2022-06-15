package com.a1tech.drugprice.Model;

public class Pharm {

    private String pharmName;
    private String thisDrugPrice;
    private String distance;
    private String drugImage;

    public Pharm(String pharmName, String thisDrugPrice, String distance, String drugImage) {
        this.pharmName = pharmName;
        this.thisDrugPrice = thisDrugPrice;
        this.distance = distance;
        this.drugImage = drugImage;
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
}
