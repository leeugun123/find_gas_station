package org.techtown.find_gas_station.presentation.common.bindingadapter

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.LoadingState
import org.techtown.find_gas_station.presentation.ui.oilroundinfo.toVisibility

@BindingAdapter("loadingStateVisibility")
fun ProgressBar.setLoadingStateVisibility(loadingState: LoadingState) {
    visibility = loadingState.toVisibility(isLoading = true)
}

@BindingAdapter("loadingStateVisibility")
fun RecyclerView.setLoadingStateVisibility(loadingState: LoadingState) {
    visibility = loadingState.toVisibility(isLoading = false)
}
