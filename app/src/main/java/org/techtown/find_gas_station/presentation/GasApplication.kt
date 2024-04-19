package org.techtown.find_gas_station.presentation

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.techtown.find_gas_station.BuildConfig


@HiltAndroidApp
class GasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKakaoApiSdk()
    }

    private fun initKakaoApiSdk() {
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}")
    }
}