package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import org.techtown.find_gas_station.R

class HighGasolineFragment : BaseOilFragment() {
    override fun getOilKindString(): Int = R.string.high_gasoline
    override fun getOilCode(): Int = R.string.high_gasoline_code
}