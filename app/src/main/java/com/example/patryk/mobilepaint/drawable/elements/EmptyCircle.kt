package com.example.patryk.mobilepaint.drawable.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableType

class EmptyCircle(paint: Paint):Drawable {
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
        paint.style = Paint.Style.STROKE
        if (initPoint != null && endPoint !=null) {
            val centerX = initPoint!!.x.toFloat()
            val centerY = initPoint!!.y.toFloat()
            val radiusX = Math.abs(centerX - endPoint!!.x)
            val radiusY = Math.abs(centerY - endPoint!!.y)
            canvas.drawCircle(centerX, centerY, Math.max(radiusX,radiusY),paint)
        }
    }
}