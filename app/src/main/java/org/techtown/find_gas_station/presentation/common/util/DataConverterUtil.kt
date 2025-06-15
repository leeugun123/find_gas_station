package org.techtown.find_gas_station.presentation.common.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.techtown.find_gas_station.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateConverterUtil @Inject constructor() {
    fun getRecentWeekDays(@ApplicationContext context: Context): Array<String> {
        return arrayOf(
            context.getString(R.string.seven_day_ago),
            context.getString(R.string.six_day_ago),
            context.getString(R.string.five_day_ago),
            context.getString(R.string.four_day_ago),
            context.getString(R.string.three_day_ago),
            context.getString(R.string.two_day_ago),
            context.getString(R.string.one_day_ago)
        )
    }
}