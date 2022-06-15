package com.a1tech.drugprice.Model;

public class Drug {

    private String drugName;
    private String drugPrice;
    private String drugImg;
    private int drugId;

    public Drug(String drugName, String drugPrice, String drugImg, int drugId) {
        this.drugName = drugName;
        this.drugPrice = drugPrice;
        this.drugImg = drugImg;
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public String getDrugPrice() {
        return drugPrice;
    }

    public String getDrugImg() {
        return drugImg;
    }

    public int getDrugId() {
        return drugId;
    }
}
