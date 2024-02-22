package org.techtown.find_gas_station.View.Activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.OilCondition.afterIntel
import org.techtown.find_gas_station.View.Fragment.DailyFragment
import org.techtown.find_gas_station.View.Fragment.HomeFragment
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.BACK_PRESS_EXIT_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantGuide.INVALID_GUIDE
import org.techtown.find_gas_station.Util.Constant.ConstantsTime.BACK_PRESS_WAIT_TIME
import org.techtown.find_gas_station.ViewModel.SetViewModel
import org.techtown.find_gas_station.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private val setViewModel : SetViewModel by viewModels()
    private val mBinding by lazy { ActivityFragmentBinding.inflate(layoutInflater) }
    private val fa by lazy { HomeFragment() }
    private val fb by lazy { DailyFragment() }
    private val fragmentManager by lazy { supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentInit()
        bottomNavigationBarInit()
    }

    private fun bottomNavigationBarInit() =

        mBinding.bottomNav.setOnItemSelectedListener { menuItem ->

            when (menuItem.itemId) {
                R.id.Home_fragment -> { showFa() }
                R.id.Daily_fragment -> { showFb() }
                else -> throw IllegalArgumentException(INVALID_GUIDE)
            }
            true
        }


    private fun showFa() {
        fragmentManager.beginTransaction().show(fa).commit()
        fragmentManager.beginTransaction().hide(fb).commit()
    }

    private fun showFb(){
        fragmentManager.beginTransaction().show(fb).commit()
        fragmentManager.beginTransaction().hide(fa).commit()
    }

    private fun fragmentInit() {
        setContentView(mBinding.root)
        addFragment()
    }

    private fun addFragment() {
        fragmentManager.beginTransaction().add(R.id.main_frame, fa).commit()
        fragmentManager.beginTransaction().add(R.id.main_frame, fb).commit()
    }

    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            localDatabaseUpdate() // local DB 수정
            //여기서 종료됨.
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, BACK_PRESS_EXIT_GUIDE, Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, BACK_PRESS_WAIT_TIME)  // 2초간 뒤로가기 버튼을 두 번 눌러야 종료되도록 설정

    }

    private fun localDatabaseUpdate() {

        insertOilData()
        finish()

    }

    private fun insertOilData() {

        setViewModel.deleteData()
        //반드시 데이터가 delete 된 후 insert 시켜야함.
        setViewModel.insertData(OilData(afterIntel[2] ,afterIntel[0] , afterIntel[1]))

    }

    override fun onConfigurationChanged(newConfig: Configuration) { super.onConfigurationChanged(newConfig) }

}