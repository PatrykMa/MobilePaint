package com.example.patryk.mobilepaint.menu_seekbar

import android.content.Context
import android.content.res.Configuration
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.patryk.mobilepaint.R
import kotlinx.android.synthetic.main.color_dialog.view.*

class MenuNavigationView:LinearLayout {

    lateinit var titleView : TextView
    lateinit var iconView : ImageView


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
        orientation = LinearLayout.HORIZONTAL
        iconView = ImageView(context)
        iconView.setImageDrawable(resources.getDrawable(R.drawable.icons8_enlarge_24 ))
        titleView = TextView(context)
        addView(titleView)
        addView(iconView)

    }

    override fun onAttachedToWindow() {
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            titleView.visibility = View.GONE
        }
        else{
            titleView.visibility = View.VISIBLE
        }
        super.onAttachedToWindow()
    }
}