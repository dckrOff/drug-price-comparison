package com.a1tech.drugprice.Utils;

import com.a1tech.drugprice.Model.Pharm;

import java.util.Comparator;

public class MyComparator implements Comparator<Pharm> {
    @Override
    public int compare(Pharm o1, Pharm o2) {
        return o1.getThisDrugPrice().compareTo(o2.getThisDrugPrice());
    }
}
