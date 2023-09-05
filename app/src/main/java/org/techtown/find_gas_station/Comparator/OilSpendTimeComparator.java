package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilSpendTimeComparator implements Comparator<OilList> {
    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.getSpendTime()) == Integer.parseInt(t2.getSpendTime())){
            return Integer.parseInt(t1.getPrice()) - Integer.parseInt(t2.getPrice());
        }
        else{
            return Integer.parseInt(t1.getSpendTime())-Integer.parseInt(t2.getSpendTime());
        }
    }


}
