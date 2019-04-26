package com.example.patryk.mobilepaint.menu_seekbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.example.patryk.mobilepaint.R
import kotlinx.android.synthetic.main.drop_down_seekbar.view.*

class SimpleSeekBar:LinearLayout {

    var onValueChange: ()->Unit = {}
    var progresValue:Int
        get() {return seekBar1.progress}
        set(value) {seekBar1.progress = value }

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
        inflate(context, R.layout.drop_down_seekbar, this)
        seekBar1?.setOnSeekBarChangeListener(Listener())
        //val par = LinearLayout.LayoutParams(250, height)
        //layoutParams = par
        //requestLayout()
    }

    inner class Listener:SeekBar.OnSeekBarChangeListener
    {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            textView1?.text = progress.toString()
            onValueChange()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    }

}