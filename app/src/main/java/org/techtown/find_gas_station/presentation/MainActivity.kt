package org.techtown.find_gas_station.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.ActivityMainBinding
import org.techtown.find_gas_station.localdatabase.OilData
import org.techtown.find_gas_station.localdatabase.SetViewModel
import org.techtown.find_gas_station.presentation.oilroundinfo.OilInfoViewModel


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
        setContentView(binding.root)

        setupWindowInsetsListener()
        setupBackPressedDispatcher()

        observeLocalData()
        observeUpdateComplete()
    }

    private fun observeLocalData() {
        setViewModel.roomDbOilCondition.observe(this) { roomDbOilCondition ->
            oilInfoViewModel.oilCondition.radius = roomDbOilCondition.oilRad.toString()
            oilInfoViewModel.oilCondition.sort = roomDbOilCondition.oilSort.toString()
            oilInfoViewModel.oilCondition.oilKind = roomDbOilCondition.oilName.toString()
        }
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
        setViewModel.updateData(
            OilData(
                oilInfoViewModel.oilCondition.oilKind,
                oilInfoViewModel.oilCondition.radius,
                oilInfoViewModel.oilCondition.sort,
            )
        )
    }

    private fun observeUpdateComplete() {
        setViewModel.updateComplete.observe(this) { complete ->
            if (complete)
                finish()
        }
    }

    companion object {
        private const val ASK_AGAIN_EXIT_DURATION = 2_000
    }
}