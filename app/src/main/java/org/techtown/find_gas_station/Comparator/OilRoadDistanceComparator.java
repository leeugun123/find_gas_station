package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilRoadDistanceComparator implements Comparator<OilList> {
    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.getActDistance()) == Integer.parseInt(t2.getActDistance())){
            return Integer.parseInt(t1.getSpendTime()) - Integer.parseInt(t2.getSpendTime());
        }else{
            return Integer.parseInt(t1.getActDistance()) - Integer.parseInt(t2.getActDistance());
        }

    }



}
