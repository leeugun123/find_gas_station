package org.techtown.find_gas_station.Util.Comparator

import org.techtown.find_gas_station.Data.TotalOilInfo


class OilPriceComparator : Comparator<TotalOilInfo> {
    override fun compare(t1: TotalOilInfo, t2: TotalOilInfo) =
        if (t1.price.toInt() == t2.price.toInt()) {
            t1.distance.toInt() - t2.distance.toInt()
        } else {
            t1.price.toInt() - t2.price.toInt()
        }

}

//가격이 같은 경우, 거리가 가까운 순으로 만들기