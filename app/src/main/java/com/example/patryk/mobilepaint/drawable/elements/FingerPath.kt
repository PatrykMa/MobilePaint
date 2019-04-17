package com.example.patryk.mobilepaint.drawable.elements

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableType

class FingerPath(paint: Paint): Drawable {
    override val type: DrawableType = DrawableType.FingerPath
    private  var path:Path = Path()
    private var mX:Float? = null
    private var mY:Float? = null
    var pointList = mutableListOf<Point>()
    override var paint: Paint = paint
    override var size: Int = 0
    override fun onTouchDown(point: Point) {
        if(mX == null)
        {
            path.reset()
            mX = point.x.toFloat()
            mY = point.y.toFloat()
            path.moveTo(mX!!,mY!!)
        }
        else {
            path.quadTo(mX!!,mY!!,point.x.toFloat(),point.y.toFloat())
            mX = point.x.toFloat()
            mY = point.y.toFloat()
        }
       //pointList.add(point)
    }

    override fun draw(canvas: Canvas) {
        paint.style = Paint.Style.STROKE
        canvas.drawPath(path,paint)
       /* pointList.forEach {
            canvas.drawCircle(it.x.toFloat(),it.y.toFloat(),paint.strokeWidth,paint)
        }*/
    }
}