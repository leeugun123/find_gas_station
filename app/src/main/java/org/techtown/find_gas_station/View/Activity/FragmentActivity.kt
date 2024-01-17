package org.techtown.find_gas_station.View.Activity

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import org.techtown.find_gas_station.View.Fragment.DailyFragment
import org.techtown.find_gas_station.View.Fragment.HomeFragment
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    private lateinit var mBinding : ActivityFragmentBinding
    private lateinit var fa : Fragment
    private lateinit var fb : Fragment
    private lateinit var fragmentManager : FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentInit()

        mBinding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->

            when (menuItem.itemId) {

                R.id.Home_fragment -> {

                    if (fa == null) {
                        fa = HomeFragment()
                        fragmentManager!!.beginTransaction().add(R.id.main_frame, fa!!).commit()
                    }
                    fragmentManager!!.beginTransaction().show(fa!!).commit()
                    fragmentManager!!.beginTransaction().hide(fb).commit()

                }

                R.id.Daily_fragment -> {

                    if (fb == null) {
                        fb = DailyFragment()
                        fragmentManager!!.beginTransaction().add(R.id.main_frame, fb!!).commit()
                    }

                    fragmentManager!!.beginTransaction().show(fb!!).commit()
                    fragmentManager!!.beginTransaction().hide(fa).commit()

                }
                else -> throw IllegalArgumentException("Invalid itemId")
            }
            true

        }

    }

    private fun fragmentInit() {

        mBinding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        fragmentManager = supportFragmentManager
        fa = HomeFragment()
        fragmentManager!!.beginTransaction().add(R.id.main_frame, fa).commit()
    }

    override fun onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)  // 2초간 뒤로가기 버튼을 두 번 눌러야 종료되도록 설정

    }

    override fun onConfigurationChanged(newConfig: Configuration) { super.onConfigurationChanged(newConfig) }

}