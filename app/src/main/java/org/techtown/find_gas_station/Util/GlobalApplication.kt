package org.techtown.find_gas_station.Util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import org.techtown.find_gas_station.BuildConfig

class GlobalApplication : Application() {

    companion object {
        private lateinit var instance : GlobalApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}")
    }

}