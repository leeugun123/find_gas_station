package org.techtown.find_gas_station.presentation.ui.home

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ActivityMainBinding
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.StationInfoViewModel

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

    private val stationInfoViewModel: StationInfoViewModel by viewModels()
    private val setViewModel: SetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initWindowSet()

        setupWindowInsetsListener()
        setupBackPressedDispatcher()

        observeLocalData()
        observeUpdateComplete()
    }

    private fun initWindowSet() {
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
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
            updateOilCondition()
        } else {
            backPressedTime = currentTime
            Toast.makeText(this, R.string.back_press_exit_guide, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateOilCondition() {
        setViewModel.updateLocalOilCondition(
            OilCondition(
                radius = stationInfoViewModel.oilCondition.radius,
                sort = stationInfoViewModel.oilCondition.sort,
                oilKind = stationInfoViewModel.oilCondition.oilKind,
            )
        )
    }

    private fun observeLocalData() {
        lifecycleScope.launch {
            setViewModel.roomDbOilCondition.collect { oilData->
                    stationInfoViewModel.oilCondition.radius = oilData.radius
                    stationInfoViewModel.oilCondition.sort = oilData.sort
                    stationInfoViewModel.oilCondition.oilKind = oilData.oilKind
            }
        }
    }

    private fun observeUpdateComplete() {
        lifecycleScope.launch {
            setViewModel.updateComplete.collect { complete ->
                if (complete)
                        finish()
            }
        }
    }

    companion object {
        private const val ASK_AGAIN_EXIT_DURATION = 2_000
    }
}