package org.techtown.find_gas_station.presentation.common.extension

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.techtown.find_gas_station.R

fun Fragment.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}

fun Fragment.showToast(content : String){
    Toast.makeText(
        requireContext(),
        content,
        Toast.LENGTH_SHORT
    ).show()
}
