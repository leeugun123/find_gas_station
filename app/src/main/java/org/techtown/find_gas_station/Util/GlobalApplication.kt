package org.techtown.find_gas_station.Util

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import org.techtown.find_gas_station.BuildConfig

object GlobalApplication : Application() {

    private val instance by lazy { this }

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}")
    }

}