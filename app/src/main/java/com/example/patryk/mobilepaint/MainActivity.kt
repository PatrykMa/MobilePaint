package com.example.patryk.mobilepaint

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.patryk.mobilepaint.drawable.DrawableType

class MainActivity : AppCompatActivity() {

    lateinit private var drawView:DrawView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setCustomView(R.layout.app_bar)
        actionBar
        drawView = findViewById<DrawView>(R.id.drawView)
        setOnClickListenerToAppBarViews()
    }
    private fun setOnClickListenerToAppBarViews()
    {
        findViewById<ImageView>(R.id.line).setOnClickListener {
            drawView.drawElementType =DrawableType.Line
        }
        findViewById<ImageView>(R.id.path).setOnClickListener {
            drawView.drawElementType =DrawableType.FingerPath
        }
        findViewById<ImageView>(R.id.circle).setOnClickListener {
            drawView.drawElementType =DrawableType.Circle
        }
        findViewById<ImageView>(R.id.pallet).setOnClickListener {
            createDialog()
        }
        findViewById<ImageView>(R.id.circleEmpty).setOnClickListener {
            drawView.drawElementType =DrawableType.EmptyCircle
        }
        findViewById<ImageView>(R.id.emptyRectangle).setOnClickListener {
            drawView.drawElementType =DrawableType.EmptyRectangle
        }
        findViewById<ImageView>(R.id.rectangle).setOnClickListener {
            drawView.drawElementType =DrawableType.Rectangle
        }
    }

    private fun createDialog(){
        val dialog = ColorPalletDialog(this).also {
            it.onPositiveClose = {drawView.paint.color = it.color}
        }

        dialog.show()
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }





}
