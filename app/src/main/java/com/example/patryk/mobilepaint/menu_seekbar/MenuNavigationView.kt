package com.example.patryk.mobilepaint.menu_seekbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

class MenuNavigationView:LinearLayout {

    lateinit var titleView : TextView


    var title:String
        get() {return titleView?.text.toString()}
        set(value) {titleView?.text = value}

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
        orientation = LinearLayout.VERTICAL
        titleView = TextView(context)
        addView(titleView)

    }
}