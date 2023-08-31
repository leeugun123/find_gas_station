package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilDistanceComparator implements Comparator<OilList> {
    @Override
    public int compare(OilList t1, OilList t2) {

        if(Integer.parseInt(t1.getDistance()) == Integer.parseInt(t2.getDistance())){
            return Integer.parseInt(t1.getPrice())-Integer.parseInt(t2.getPrice());
        }
        else{
            return Integer.parseInt(t1.getDistance()) - Integer.parseInt(t2.getDistance());
        }
    }
}
//거리순 오름차순 정렬
//만약 거리가 같다면 가격이 같은 순으로 만들기

