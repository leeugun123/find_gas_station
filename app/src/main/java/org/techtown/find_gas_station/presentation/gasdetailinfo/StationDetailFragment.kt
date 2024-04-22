package org.techtown.find_gas_station.presentation.gasdetailinfo

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentStationDetailBinding
import org.techtown.find_gas_station.presentation.BaseFragment

class StationDetailFragment :
    BaseFragment<FragmentStationDetailBinding>(R.layout.fragment_station_detail),
    OnMapReadyCallback {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

}