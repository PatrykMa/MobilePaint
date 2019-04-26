package com.example.patryk.mobilepaint

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.SeekBar
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.ShapeDrawable
import android.graphics.Shader.TileMode
import android.graphics.LinearGradient
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.core.graphics.ColorUtils


class AlfeGradientSeekBar:SeekBar {
    companion object {
        private var seekBarPositions = 100;
    }
    var onColorChange:()->Unit ={}
    var color:Int = 0
    set(value){
        field = value

        val test = LinearGradient(
            0f, 0f, 300f, 0.0f,

            intArrayOf(-0x1000000, -0xffff01, -0xff0100, -0xff0001, -0x10000, -0xff01, -0x100, -0x1),
            null, TileMode.CLAMP
        )
        val shape = ShapeDrawable(RectShape())
        progressDrawable = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
            intArrayOf( ColorUtils.setAlphaComponent(value, 0),
                ColorUtils.setAlphaComponent(value, 255)))
        shape.paint.shader = test


       //progressDrawable = shape
    }
    get() {return ColorUtils.setAlphaComponent(field, progress*255/100)}

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
        //val a = context.obtainStyledAttributes(
        //    attrs, R.styleable.DrawView, defStyle, 0
        //)
        //max = 1
        //max
        color = Color.BLACK
        //a.recycle()
        setOnSeekBarChangeListener(Listener())
        progress = seekBarPositions

    }
    inner class Listener:SeekBar.OnSeekBarChangeListener
    {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            seekBarPositions = progress
            onColorChange()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}