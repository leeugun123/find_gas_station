package org.techtown.find_gas_station.presentation.common.util.comparator

import org.techtown.find_gas_station.domain.model.StationDetailInfo


class OilSpendTimeComparator : Comparator<StationDetailInfo> {
    override fun compare(t1 : StationDetailInfo, t2 : StationDetailInfo) =
        if (t1.spendTime.toInt() == t2.spendTime.toInt()) {
            t1.price.toInt() - t2.price.toInt()
        } else {
            t1.spendTime.toInt() - t2.spendTime.toInt()
        }

}