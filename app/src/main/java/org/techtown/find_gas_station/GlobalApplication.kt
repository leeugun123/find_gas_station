package org.techtown.find_gas_station

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import org.techtown.find_gas_station.BuildConfig

object GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}" )
    }

}