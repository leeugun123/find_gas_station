package org.techtown.find_gas_station;

import java.util.Comparator;

public class OilDistanceComparator implements Comparator<oil_list> {
    @Override
    public int compare(oil_list oil_list, oil_list t1) {

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

