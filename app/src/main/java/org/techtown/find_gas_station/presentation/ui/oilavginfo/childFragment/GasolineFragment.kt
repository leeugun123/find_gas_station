package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import org.techtown.find_gas_station.R

class GasolineFragment : BaseOilFragment() {
    override fun getOilKindString(): Int = R.string.gasoline
    override fun getOilCode(): Int = R.string.gasoline_code
}

