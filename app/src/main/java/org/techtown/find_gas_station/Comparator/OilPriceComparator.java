package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilPriceComparator implements Comparator<OilList> {

    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.getPrice()) == Integer.parseInt(t2.getPrice())){
            return Integer.parseInt(t1.getDistance()) - Integer.parseInt(t2.getDistance());
        }
        else{
            return Integer.parseInt(t1.getPrice())-Integer.parseInt(t2.getPrice());
        }

    }
    //가격이 같은 경우, 거리가 가까운 순으로 만들기
}
