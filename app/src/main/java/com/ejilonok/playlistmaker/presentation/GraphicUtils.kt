package com.ejilonok.playlistmaker.presentation

import android.util.TypedValue
import android.view.View

class GraphicUtils {
    companion object {
        fun dpToPx(dp: Float, view : View): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                view.context.resources.displayMetrics).toInt()
        }
    }
}