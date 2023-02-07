package org.techtown.find_gas_station;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        KakaoSdk.init(this,"{"+ BuildConfig.KAKAO_API_KEY + "}");

    }



}
