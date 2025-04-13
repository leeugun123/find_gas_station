package org.techtown.find_gas_station

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import org.techtown.find_gas_station.di.RepositoryModule


@HiltAndroidApp
class GasApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "{" + BuildConfig.KAKAO_API_KEY + "}")
    }
}

