package org.techtown.find_gas_station.Util.Comparator

import org.techtown.find_gas_station.Data.TotalOilInfo


class OilRoadDistanceComparator : Comparator<TotalOilInfo> {
    override fun compare(t1 : TotalOilInfo, t2 : TotalOilInfo) =
        if (t1.actDistance.toInt() == t2.actDistance.toInt()) {
            t1.price.toInt() - t2.price.toInt()
        } else {
            t1.actDistance.toInt() - t2.actDistance.toInt()
        }

}