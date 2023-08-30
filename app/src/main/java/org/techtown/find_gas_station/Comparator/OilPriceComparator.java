package org.techtown.find_gas_station.Comparator;

import org.techtown.find_gas_station.OilList;

import java.util.Comparator;

public class OilPriceComparator implements Comparator<OilList> {

    @Override
    public int compare(OilList oil_list, OilList t1) {

        if(Integer.parseInt(oil_list.getPrice()) == Integer.parseInt(t1.getPrice())){
            return Integer.parseInt(oil_list.getDistance()) - Integer.parseInt(t1.getDistance());
        }
        else{
            return Integer.parseInt(oil_list.getPrice())-Integer.parseInt(t1.getPrice());
        }

    }
    //가격이 같은 경우, 거리가 가까운 순으로 만들기
}
