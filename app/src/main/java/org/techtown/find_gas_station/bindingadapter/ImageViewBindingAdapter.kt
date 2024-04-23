package org.techtown.find_gas_station.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import org.techtown.find_gas_station.R

@BindingAdapter("app:setImg")
fun ImageView.setImg(img: Int) {
    setImageResource(img)
}

@BindingAdapter("app:setCarWashImg")
fun ImageView.setCarWashImg(carWash : String) {
    if(carWash == "Y")
        setImageResource(R.drawable.car_wash)
}

@BindingAdapter("app:setConStoreImg")
fun ImageView.setConStoreImg(conStore : String) {
    if(conStore == "Y")
        setImageResource(R.drawable.convenstore)
}