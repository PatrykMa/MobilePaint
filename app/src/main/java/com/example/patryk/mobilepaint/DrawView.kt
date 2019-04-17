package com.example.patryk.mobilepaint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableFactory
import com.example.patryk.mobilepaint.drawable.DrawableType
import com.example.patryk.mobilepaint.drawable.elements.FingerPath
import com.example.patryk.mobilepaint.drawable.elements.Line
import com.example.patryk.mobilepaint.drawable.symetry.SymetricDrawer
import com.example.patryk.mobilepaint.drawable.symetry.SymetricType

class DrawView:View {
    private var elements = mutableListOf<Drawable>()
    var paint: Paint
    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 10f
    }
    var symetricType = SymetricType.None
        set(value) {field = value
            invalidate()}
    var drawElementType: DrawableType = DrawableType.Line


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.DrawView, defStyle, 0
        )
        a.recycle()

    }
    private var mX = 0f
    private var mY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                mX = event.x
                mY = event.y
                addElement()
                elements.last().onTouchDown(Point(mX.toInt(),mY.toInt()))
            }
            MotionEvent.ACTION_MOVE -> {
                elements.last().onTouchDown(Point((event.x).toInt(),( event.y).toInt()))
            }
        }
        invalidate()
        return true
    }

    private fun addElement()
    {
        if (symetricType == SymetricType.None)
            elements.add(DrawableFactory.createObject(Paint(paint), drawElementType))
        else
            elements.add(SymetricDrawer(symetricType,drawElementType,Paint(paint),this.width,this.height))
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            elements.forEach {
                it.draw(canvas)
            }
            SymetricDrawer.drawAxis(symetricType,canvas)
        }

        super.onDraw(canvas)
    }
}