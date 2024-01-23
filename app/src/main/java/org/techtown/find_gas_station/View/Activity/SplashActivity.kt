package org.techtown.find_gas_station.View.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.LOCATION_PERMISSION_REQUEST_CODE
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.SPLASH_WAIT_TIME
import org.techtown.find_gas_station.ViewModel.SetViewModel

class SplashActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        if (checkLocationPermission()) {
            splashAction()
        }// 위치 권한이 허용된 경우 , 다음 화면으로 이동
        else { requestLocationPermission() }
        // 위치 권한 요청

    }

    // 위치 권한이 허용되었는지 확인하는 메소드
    private fun checkLocationPermission() = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)

    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)

    }
    // 위치 권한 요청 메소드


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                splashAction()
            } else {
                Toast.makeText(this, "위치 권한이 거부되었습니다. 앱을 종료합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }

    // 다음 화면으로 이동하는 메소드
    private fun startNextActivity() {
        startActivity(Intent(this, FragmentActivity::class.java))
        finish()
    }

    private fun splashAction(){
        Handler().postDelayed(Runnable { startNextActivity() }, SPLASH_WAIT_TIME)
    }

}