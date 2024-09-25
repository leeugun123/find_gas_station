package org.techtown.find_gas_station.presentation.ui.splash

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.databinding.FragmentSplashBinding
import org.techtown.find_gas_station.presentation.common.base.BaseFragment
import org.techtown.find_gas_station.presentation.common.extension.showToast

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) splashAction() else finishApp()
        }

    private fun splashAction() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(SPLASH_WAIT_TIME)
            navigateMainFragment()
        }
    }

    private fun navigateMainFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    private fun finishApp() {
        showToast(requireContext().getString(R.string.access_denied))
        requireActivity().finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }

    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    companion object {
        private const val SPLASH_WAIT_TIME: Long = 1500 //1.5초
    }
}