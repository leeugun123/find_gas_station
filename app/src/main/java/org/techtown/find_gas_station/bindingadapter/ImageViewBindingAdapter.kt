package org.techtown.find_gas_station.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("app:setImg")
fun ImageView.setImg(img: Int) {
    setImageResource(img)
}