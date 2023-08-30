package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilDistanceComparator implements Comparator<OilList> {
    @Override
    public int compare(OilList oil_list, OilList t1) {

        if(Integer.parseInt(oil_list.getDistance()) == Integer.parseInt(t1.getDistance())){
            return Integer.parseInt(oil_list.getPrice())-Integer.parseInt(t1.getPrice());
        }
        else{
            return Integer.parseInt(oil_list.getDistance()) - Integer.parseInt(t1.getDistance());
        }
    }
}
//거리순 오름차순 정렬
//만약 거리가 같다면 가격이 같은 순으로 만들기

