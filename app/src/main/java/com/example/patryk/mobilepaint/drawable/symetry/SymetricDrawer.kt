package com.example.patryk.mobilepaint.drawable.symetry

import android.graphics.*
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableFactory
import com.example.patryk.mobilepaint.drawable.DrawableType
import java.lang.Exception
import kotlin.math.abs


class SymetricDrawer(var symetricType: SymetricType, drawableType: DrawableType, paint: Paint,
                     var width:Int, var height:Int): Drawable {
    private var drawingObjectArray:Array<Drawable>

    init {
        drawingObjectArray = when(symetricType)
        {
            SymetricType.None -> arrayOf()
            SymetricType.Vertical-> {Array(2, { i -> DrawableFactory.createObject(paint,drawableType) })}
            SymetricType.Horizontal-> {Array(2, { i -> DrawableFactory.createObject(paint,drawableType) })}
            SymetricType.TwoAxis-> {Array(4, { i -> DrawableFactory.createObject(paint,drawableType) })}
            SymetricType.FourAxis-> {Array(8, { i -> DrawableFactory.createObject(paint,drawableType) })}
        }
    }

    override var paint: Paint = paint
    override var size: Int = 0
    override val type: DrawableType = drawableType

    override fun onTouchDown(point: Point) {
        //val pointInFirstQarter = getPositionInFirstQuarter(point)

        getPointsArray(point).forEachIndexed { index, point ->
            drawingObjectArray[index].onTouchDown(point)
        }
    }

    private fun getPointsArray(point: Point):Array<Point>
    {
        return when(symetricType) {
            SymetricType.None -> arrayOf()
            SymetricType.Vertical -> arrayOf(point, reversesPoint(point))
            SymetricType.Horizontal -> arrayOf(point, reversesPoint(point))
            SymetricType.TwoAxis ->pointsForTwoAxis(point)
            SymetricType.FourAxis -> pointsForFourAxis(point)
        }
    }
    private fun pointsForFourAxis(point: Point):Array<Point>
    {
        val arr = pointsForTwoAxis(point)
        val lefTopPoint = getTopLeftPoint(arr)
        return arr.toMutableList().also { it->
            pointsForTwoAxis(Point(lefTopPoint.y, lefTopPoint.x)).forEach { point ->
                it.add(point)
            }
        }.toTypedArray()

    }

    private fun getTopLeftPoint(points:Array<Point>):Point
    {
        points.forEach {
            if(it.x<= width/2 && it.y <= height/2)
                return it
        }
        throw Exception("upssi dupsi nie ma akiego elementu")
    }

    private fun pointsForTwoAxis(point: Point):Array<Point>
    {
        return arrayOf(
            point,
            Point(point.x, height-point.y),
            Point(width - point.x, point.y),
            Point(width - point.x, height-point.y)
        )
    }

    private fun reversesPoint(point: Point):Point
    {
        if (symetricType == SymetricType.Horizontal)
        {
            return Point(point.x, height-point.y)
        }
        else// if(symetricType == SymetricType.Vertical)
            return Point(width - point.x, point.y)
    }

    override fun draw(canvas: Canvas) {
        drawingObjectArray.forEach {
            it.draw(canvas)
        }
    }

    private fun getPositionInFirstQuarter(point: Point):Point
    {
        if(symetricType == SymetricType.Horizontal)
        {
            if(point.y > height/2)
            {
                return Point(point.x,Math.abs(point.y - height))
            }
            return Point(point.x,point.y)
        }
        else {
            if(point.x > width/2)
            {
                return Point(Math.abs(point.x - width),point.y)
            }
            return Point(point.x ,point.y)
        }
    }
    companion object {
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG).also {  it.color = Color.BLACK;it.alpha = 60;
            it.style = Paint.Style.STROKE
            it.strokeWidth = 5f;
            it.pathEffect = DashPathEffect(floatArrayOf(12f,20f),0f)
            it.isAntiAlias = true
            }
        fun drawAxis(type:SymetricType, canvas: Canvas)
        {
            val path = Path()
            when(type)
            {
                SymetricType.Horizontal ->
                {
                    path.moveTo(0f,canvas.height/2f)
                    path.quadTo(0f,canvas.height/2f,canvas.width.toFloat(),canvas.height/2f)
                    canvas.drawPath(path, paint)
                    //canvas.drawLine(0f,canvas.height/2f, canvas.width.toFloat(),canvas.height/2f , paint)
                }
                SymetricType.Vertical ->
                {
                    path.moveTo(canvas.width/2f,0f)
                    path.quadTo(canvas.width/2f,0f,canvas.width/2f,canvas.height.toFloat())
                    canvas.drawPath(path, paint)
                    //canvas.drawLine(canvas.width/2f, 0f, canvas.width/2f,canvas.height.toFloat(), paint)
                }
                SymetricType.None -> {}
                else ->
                {
                    path.moveTo(0f,canvas.height/2f)
                    path.quadTo(0f,canvas.height/2f,canvas.width.toFloat(),canvas.height/2f)
                    path.moveTo(canvas.width/2f,0f)
                    path.quadTo(canvas.width/2f,0f,canvas.width/2f,canvas.height.toFloat())
                    canvas.drawPath(path, paint)
                    //canvas.drawLine(0f,canvas.height/2f, canvas.width.toFloat(),canvas.height/2f , paint )
                    //canvas.drawLine(canvas.width/2f, 0f, canvas.width/2f,canvas.height.toFloat(), paint)
                }
            }
        }
    }

}