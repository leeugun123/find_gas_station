package org.techtown.find_gas_station.presentation.ui.oilavginfo.childFragment

import org.techtown.find_gas_station.R

class DieselFragment : BaseOilFragment() {
    override fun getOilKindString(): Int = R.string.diesel
    override fun getOilCode(): Int = R.string.diesel_code
}