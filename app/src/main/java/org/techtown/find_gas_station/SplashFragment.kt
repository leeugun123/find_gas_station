package org.techtown.find_gas_station

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.databinding.FragmentSplashBinding
import org.techtown.find_gas_station.util.constant.ConstantsTime

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                splashAction()
            } else {
                finishApp()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }
    private fun requestPermission() {
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun finishApp() {
        Toast.makeText(requireContext(), R.string.access_denied, Toast.LENGTH_SHORT).show()
        requireActivity().finish()
    }

    private fun splashAction() {
        lifecycleScope.launch(Dispatchers.Main) {
            delay(SPLASH_WAIT_TIME)
            moveToMainFragment()
        }
    }

    private fun moveToMainFragment() {
        findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
    }

    companion object {
        private const val SPLASH_WAIT_TIME : Long = 1500 //1.5초
    }
}