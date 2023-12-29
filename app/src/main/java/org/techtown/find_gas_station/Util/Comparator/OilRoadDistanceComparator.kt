package org.techtown.find_gas_station.Util.Comparator

import org.techtown.find_gas_station.Data.kakaoResponseModel.TotalOilInfo


class OilRoadDistanceComparator : Comparator<TotalOilInfo> {
    override fun compare(t1 : TotalOilInfo, t2 : TotalOilInfo) =
        if (t1.getActDistance().toInt() == t2.getActDistance().toInt()) {
            t1.getPrice().toInt() - t2.getPrice().toInt()
        } else {
            t1.getActDistance().toInt() - t2.getActDistance().toInt()
        }

}