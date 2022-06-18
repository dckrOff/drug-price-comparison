package com.a1tech.drugprice.Model;

public class Drug {

    private final String drugName;
    private final String drugPrice;
    private final String drugImg;
    private final String drugId;

    public Drug(String drugName, String drugPrice, String drugImg, String drugId) {
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

    public String getDrugId() {
        return drugId;
    }
}
