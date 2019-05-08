package com.example.patryk.mobilepaint

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.KeyEvent.KEYCODE_BACK
import android.R.attr.description
import android.widget.TextView
import android.R.attr.data
import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.skydoves.colorpickerpreference.ColorPickerView
import android.widget.LinearLayout
import com.google.android.material.button.MaterialButton
import com.skydoves.colorpickerpreference.ColorEnvelope
import com.skydoves.colorpickerpreference.ColorListener
import kotlinx.android.synthetic.main.activity_main.view.*
import android.graphics.drawable.ColorDrawable
import java.lang.Exception


class ColorPalletDialog(context: Context): AlertDialog(context) {

    private var pallet:ColorPickerView? = null

    var color = 0
        set(value) {
            field = value
            findViewById<AlfeGradientSeekBar>(R.id.alfeGradientSeekBar)?.color = value
            findViewById<ImageView>(R.id.imageView_color)?.setColorFilter(findViewById<AlfeGradientSeekBar>(R.id.alfeGradientSeekBar)!!.color)

        }
        get() {
            try {
                return findViewById<AlfeGradientSeekBar>(R.id.alfeGradientSeekBar)!!.color
            }
            catch (e:Exception) {
               return field
            }
        }

    var onPositiveClose:()->Unit = {}
    var onNegativeEnd: ()->Unit = {}
    override fun onCreate(savedInstanceState: Bundle?) {
        val content = LayoutInflater.from(context).inflate(R.layout.color_dialog, null)
        setView(content)
        //setTitle("Some Title")
        //setMessage("Some Message")
        //setButton(
        //    DialogInterface.BUTTON_POSITIVE, "Ok"
        //) { dialog, which -> onPositiveClose() }
        //setButton( DialogInterface.BUTTON_NEGATIVE, "Ok"){ dialog, which -> onNegativeEnd() }
        //(content.findViewById(R.id.data) as TextView).setText(R.string.some_custom_data)
        //(content.findViewById(R.id.description) as TextView).text = context.getString(R.string.description)
        //setCancelable(false)
        setOnKeyListener { dialog, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK }
        super.onCreate(savedInstanceState)
        pallet = findViewById<ColorPickerView>(R.id.colorPickerView_pallet)
        pallet?.preferenceName = "mainPallet"
        findViewById<ImageView>(R.id.imageView_color)?.setColorFilter(color)

        pallet?.setColorListener { colorEnvelope ->
            color = colorEnvelope.color

        }
        findViewById<MaterialButton>(R.id.material_text_poitive)?.setOnClickListener {
            onPositiveClose()
            pallet?.saveData()
            dismiss()
        }
        findViewById<MaterialButton>(R.id.material_text_negative)?.setOnClickListener {
            onNegativeEnd()
            cancel()
        }

        val alphaSeekBar = findViewById<AlfeGradientSeekBar>(R.id.alfeGradientSeekBar)
        alphaSeekBar?.onColorChange = {
            this.color = alphaSeekBar!!.color
        }

        val set = findViewById<LinearLayout>(R.id.colorSet1)
        if (set != null)
            for (i in 0 until set.childCount)
            {
                set.getChildAt(i).setOnClickListener {
                    val background = it.background
                    if (background is ColorDrawable)
                        color = (background as ColorDrawable).color
                }
            }
        val set2 = findViewById<LinearLayout>(R.id.colorSet2)
        if (set2 != null)
            for (i in 0 until set2.childCount)
            {
                set2.getChildAt(i).setOnClickListener {
                    val background = it.background
                    if (background is ColorDrawable)
                        color = (background as ColorDrawable).color
                }
            }

    }

}