package org.techtown.find_gas_station

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKakaoApiSdk()
    }

    private fun initKakaoApiSdk() {
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}")
    }
}