package org.techtown.find_gas_station.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED, block)
    }
}