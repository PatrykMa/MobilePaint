package com.example.patryk.mobilepaint.drawable

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point

interface Drawable {
    companion object {
        var backGroundColor:Int
            get() {return  backGroundPaint.color}
            set(value) {
                backGroundPaint.color = value}
        var backGroundPaint = Paint()
    }

    var paint:Paint
    var size:Int

    val type:DrawableType

    fun onTouchDown(point:Point)
    fun draw(canvas: Canvas)



}