package org.techtown.find_gas_station

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

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