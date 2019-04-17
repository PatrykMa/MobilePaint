package com.example.patryk.mobilepaint.drawable.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableType

class Line(paint: Paint): Drawable {
    override val type: DrawableType = DrawableType.Line

    private var initPoint:Point? = null
    private var endPoint:Point? = null

    override var paint: Paint = paint
    override var size: Int = 0
    override fun onTouchDown(point: Point) {
        if (initPoint == null)
            initPoint = point
        endPoint = point
    }

    override fun draw(canvas: Canvas) {
        if (initPoint != null)
            canvas.drawLine(initPoint!!.x.toFloat(), initPoint!!.y.toFloat(),endPoint!!.x.toFloat(),endPoint!!.y.toFloat(),paint)

    }
}