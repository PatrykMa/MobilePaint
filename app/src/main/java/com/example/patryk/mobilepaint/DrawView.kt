package com.example.patryk.mobilepaint

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.PixelCopy
import android.view.View
import android.view.Window
import com.example.patryk.mobilepaint.drawable.Drawable
import com.example.patryk.mobilepaint.drawable.DrawableFactory
import com.example.patryk.mobilepaint.drawable.DrawableType
import com.example.patryk.mobilepaint.drawable.elements.FingerPath
import com.example.patryk.mobilepaint.drawable.elements.Line
import com.example.patryk.mobilepaint.drawable.symetry.SymetricDrawer
import com.example.patryk.mobilepaint.drawable.symetry.SymetricType
import android.graphics.Bitmap
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth





class DrawView:View {
    private var elements = mutableListOf<Drawable>()
    private var removeElements = mutableListOf<Drawable>()
    var paint: Paint
    init {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 100f
        this.setDrawingCacheEnabled(true);

    }
    var symetricType = SymetricType.None
        set(value) {field = value
            invalidate()}
    var drawElementType: DrawableType = DrawableType.Line
    var backgroundBitmap:Bitmap? =null
        set(value) {field = value;clear()}
    var getAsBitmap:Bitmap
    get(){
        //toDo do it better :(

        val sym = symetricType
        symetricType = SymetricType.None
        val paintWhite = Paint().also { it.color = Color.WHITE }
        val bmOverlay = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmOverlay)
        canvas.drawRect(0f,0f,width.toFloat(),height.toFloat(),paintWhite)
        canvas.drawBitmap(this.getDrawingCache(), Matrix(), null)
        symetricType = sym
        return  bmOverlay

    }
    private set(value) {}

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }
     var canUndo:Boolean
        set(value) {}
        get() {return elements.isNotEmpty()}
    fun undo()
    {
        if (elements.isNotEmpty())
        {
            removeElements.add(elements.last())
            elements.removeAt(elements.size-1)
        }
        invalidate()
    }
    var canRedo:Boolean
        set(value) {}
        get() {return removeElements.isNotEmpty()}
    fun redo()
    {
        if(removeElements.isNotEmpty())
        {
            elements.add(removeElements.last())
            removeElements.removeAt(removeElements.size-1)
        }
        invalidate()
    }

    fun clear()
    {
        elements.clear()
        removeElements.clear()
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
                removeElements.clear()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                elements.last().onTouchDown(Point((event.x).toInt(),( event.y).toInt()))
            }
        }
        invalidate()
        return false
    }

    private fun addElement()
    {
        if (symetricType == SymetricType.None)
            elements.add(DrawableFactory.createObject(Paint(paint), drawElementType))
        else
            elements.add(SymetricDrawer(symetricType,drawElementType,Paint(paint),this.width,this.height))
    }

    private val backgrounPaint = Paint()
    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            if(backgroundBitmap != null)
                canvas.drawBitmap(backgroundBitmap!!,0f, 0f,backgrounPaint)
            elements.forEach {
                it.draw(canvas)
            }
            SymetricDrawer.drawAxis(symetricType,canvas)
        }

        super.onDraw(canvas)
    }
}