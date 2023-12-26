package org.techtown.find_gas_station.Util.Comparator

import org.techtown.find_gas_station.OilList

class OilPriceComparator : Comparator<OilList> {
    override fun compare(t1: OilList, t2: OilList) =
        if (t1.getPrice().toInt() == t2.getPrice().toInt()) {
            t1.getDistance().toInt() - t2.getDistance().toInt()
        } else {
            t1.getPrice().toInt() - t2.getPrice().toInt()
        }

}

//가격이 같은 경우, 거리가 가까운 순으로 만들기