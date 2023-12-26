package org.techtown.find_gas_station.Util.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilSpendTimeComparator implements Comparator<OilList> {
    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.spendTime) == Integer.parseInt(t2.spendTime)){
            return Integer.parseInt(t1.price) - Integer.parseInt(t2.price);
        }
        else{
            return Integer.parseInt(t1.spendTime)-Integer.parseInt(t2.spendTime);
        }
    }


}
