package org.techtown.find_gas_station

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import org.techtown.find_gas_station.MVVM.SetViewModel
import org.techtown.find_gas_station.set.Set

class Splash : AppCompatActivity(), OnRequestPermissionsResultCallback {


    private var setViewModel: SetViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        setViewModelInit()

        if (checkLocationPermission()) {
            Handler().postDelayed(Runnable { startNextActivity() }, 500) // 위치 권한이 허용된 경우, 다음 화면으로 이동
        } else { requestLocationPermission() }// 위치 권한 요청

    }

    private fun setViewModelInit(){
        setViewModel = ViewModelProvider(this)[SetViewModel::class.java]
        setViewModel!!.insert(Set("B027", "1000", "1"))
    }

    override fun onPause() {
        super.onPause()
    }

    // 위치 권한이 허용되었는지 확인하는 메소드
    private fun checkLocationPermission() = (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)


    // 위치 권한 요청 메소드
    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Handler().postDelayed(Runnable { startNextActivity() }, 500)
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }

}