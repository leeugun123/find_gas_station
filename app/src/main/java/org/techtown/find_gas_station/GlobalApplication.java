package org.techtown.find_gas_station;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;

        KakaoSdk.init(this,"{0405879d330b7953d9dd1ac469669ba7}");

    }



}
