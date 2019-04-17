package com.example.patryk.mobilepaint.drawable.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableType

class Rectangle(paint: Paint):Drawable {
    override val type: DrawableType = DrawableType.Circle
    override var paint= paint
    override var size = 0

    private var initPoint: Point? = null
    private var endPoint: Point? = null

    override fun onTouchDown(point: Point) {
        if (initPoint == null)
            initPoint = point
        endPoint = point
    }

    override fun draw(canvas: Canvas) {
        if (initPoint != null && endPoint !=null) {
            val biggerX = Math.max(initPoint!!.x,endPoint!!.x).toFloat()
            val biggerY = Math.max(initPoint!!.y,endPoint!!.y).toFloat()
            val smallerX = Math.min(initPoint!!.x,endPoint!!.x).toFloat()
            val smallerY = Math.min(initPoint!!.y,endPoint!!.y).toFloat()
            canvas.drawRect(smallerX, smallerY,biggerX,biggerY,paint)
        }
    }
}