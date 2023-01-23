package org.techtown.find_gas_station;

import java.util.Comparator;

public class OilPriceComparator implements Comparator<oil_list> {

    @Override
    public int compare(oil_list oil_list, oil_list t1) {

        if(Integer.parseInt(oil_list.getPrice()) == Integer.parseInt(t1.getPrice())){
            return Integer.parseInt(oil_list.getDistance()) - Integer.parseInt(t1.getDistance());
        }
        else{
            return Integer.parseInt(oil_list.getPrice())-Integer.parseInt(t1.getPrice());
        }

    }
    //가격이 같은 경우, 거리가 가까운 순으로 만들기
}
