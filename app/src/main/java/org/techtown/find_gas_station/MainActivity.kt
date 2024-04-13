package org.techtown.find_gas_station

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.databinding.ActivityMainBinding
import org.techtown.find_gas_station.localdatabase.OilData
import org.techtown.find_gas_station.localdatabase.SetViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.mainScreen) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }

    private var backPressedTime: Long = 0

    private val setViewModel: SetViewModel by viewModels()
    private val oilInfoViewModel: OilInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        setupWindowInsetsListener()
        setupBackPressedDispatcher()
        getLocalOilCondition()


    }


    private fun setupWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback {
            handleBackPressed()
        }
    }

    private fun handleBackPressed() {
        val destination = navController.currentDestination ?: return
        if (destination.id == R.id.mainFragment) finishSoftly() else navController.navigateUp()
    }

    private fun finishSoftly() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < ASK_AGAIN_EXIT_DURATION) {
            updateOilCondition() // 이 작업이 끝나고 종료 되어야 함.
            finish()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, R.string.back_press_exit_guide, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getLocalOilCondition() {
        oilInfoViewModel.oilConditionList[0] = setViewModel.localOilCondition.oilRad
        oilInfoViewModel.oilConditionList[1] = setViewModel.localOilCondition.oilSort
        oilInfoViewModel.oilConditionList[2] = setViewModel.localOilCondition.oilName
    }

    private fun updateOilCondition() {
        setViewModel.updateData(
            OilData(
                oilInfoViewModel.oilConditionList[0],
                oilInfoViewModel.oilConditionList[1],
                oilInfoViewModel.oilConditionList[2]
            )
        )
    }

    companion object {
        private const val ASK_AGAIN_EXIT_DURATION = 2_000
    }
}