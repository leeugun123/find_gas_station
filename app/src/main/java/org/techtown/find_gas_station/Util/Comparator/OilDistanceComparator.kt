package org.techtown.find_gas_station.Util.Comparator

import org.techtown.find_gas_station.OilList

class OilDistanceComparator : Comparator<OilList> {
    override fun compare(t1 : OilList, t2 : OilList) =
        if (t1.getDistance().toInt() == t2.getDistance().toInt()) {
            t1.getPrice().toInt() - t2.getPrice().toInt()
        } else {
            t1.getDistance().toInt() - t2.getDistance().toInt()
        }


}