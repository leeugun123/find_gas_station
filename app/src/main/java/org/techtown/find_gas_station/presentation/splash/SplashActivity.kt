package org.techtown.find_gas_station.presentation.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.presentation.FragmentActivity
import org.techtown.find_gas_station.util.constant.ConstantsTime.LOCATION_PERMISSION_REQUEST_CODE
import org.techtown.find_gas_station.util.constant.ConstantsTime.SPLASH_WAIT_TIME

class SplashActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        if (checkLocationPermission())
            splashAction()
        else
            requestLocationPermission()

    }

    // 위치 권한이 허용되었는지 확인하는 메소드
    private fun checkLocationPermission() = (ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED)

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                splashAction()
            else
                finishApp()
        }
    }

    private fun finishApp() {
        Toast.makeText(this, LOCATION_PERMISSION_DENIED_FINISH_APP, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun startNextActivity() {
        startActivity(Intent(this, FragmentActivity::class.java))
        finish()
    }

    private fun splashAction() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(SPLASH_WAIT_TIME)
            startNextActivity()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_DENIED_FINISH_APP = "위치 권한이 거부 되었습니다. 앱을 종료 합니다."
    }
}