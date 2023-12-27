package org.techtown.find_gas_station

import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.techtown.find_gas_station.Fragment.DailyFragment
import org.techtown.find_gas_station.Fragment.HomeFragment
import org.techtown.find_gas_station.databinding.ActivityFragmentBinding

class FragmentActivity : AppCompatActivity() {

    private var pressedTime = 0
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

        if (System.currentTimeMillis() > pressedTime + 2000) {
            pressedTime = System.currentTimeMillis().toInt()
            Toast.makeText(this, "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "종료 완료", Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) { super.onConfigurationChanged(newConfig) }

}