package org.techtown.find_gas_station.GPS

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.content.ContextCompat

class GpsTracker(private val mContext: Context) : LocationListener {

    private var locationManager: LocationManager? = null
    private var location: Location? = null

    init { getLocation() }

    private fun getLocation(): Location? {

        try {
            locationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                return null
            }

            val fineLocationPermission =
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission != PackageManager.PERMISSION_GRANTED) { return null }

            if (isNetworkEnabled) {
                locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this)
                location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (isGPSEnabled && location == null) {

                locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                    this)

                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            }

        } catch (e: Exception) { }

        return location

    }

    fun getLatitude() = location?.latitude ?: 0.0

    fun getLongitude() = location?.longitude ?: 0.0

    override fun onLocationChanged(location: Location) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
}

private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
