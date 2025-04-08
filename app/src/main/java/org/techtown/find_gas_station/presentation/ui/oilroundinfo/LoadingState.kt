package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import android.view.View

enum class LoadingState{
    INIT,
    LOADING,
    COMPLETE
}

fun LoadingState.toVisibility(isLoading: Boolean): Int =
    when (this) {
        LoadingState.LOADING -> if (isLoading) View.VISIBLE else View.GONE
        else -> if (!isLoading) View.VISIBLE else View.GONE
    }