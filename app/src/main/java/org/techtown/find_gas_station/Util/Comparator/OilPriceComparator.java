package org.techtown.find_gas_station.Util.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilPriceComparator implements Comparator<OilList> {

    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.price) == Integer.parseInt(t2.price)){
            return Integer.parseInt(t1.distance) - Integer.parseInt(t2.distance);
        }
        else{
            return Integer.parseInt(t1.price)-Integer.parseInt(t2.price);
        }

    }
    //가격이 같은 경우, 거리가 가까운 순으로 만들기
}
