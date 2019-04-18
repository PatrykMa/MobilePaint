package com.example.patryk.mobilepaint

import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.example.patryk.mobilepaint.drawable.DrawableType
import com.example.patryk.mobilepaint.drawable.symetry.SymetricType

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
        refreshRedoAndUndo()
    }
    private fun setOnClickListenerToAppBarViews()
    {
        findViewById<ImageView>(R.id.back).setOnClickListener {
            drawView.undo()
            refreshRedoAndUndo()
        }
        findViewById<ImageView>(R.id.forward).setOnClickListener {
            drawView.redo()
            refreshRedoAndUndo()
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_UP ->
            {
                refreshRedoAndUndo()
            }
        }

        return super.onTouchEvent(event)
    }
    private fun refreshRedoAndUndo()
    {

        val backIco = findViewById<ImageView>(R.id.back)
        val forwardIco = findViewById<ImageView>(R.id.forward)
        if (drawView.canUndo){
            backIco.setColorFilter(drawView.paint.color)
        }
        else{
            backIco.setColorFilter(Color.GRAY)
        }

        if (drawView.canRedo)
        {
            forwardIco.setColorFilter(drawView.paint.color)
        }
        else{
            forwardIco.setColorFilter(Color.GRAY)
        }

        backIco.invalidate()
        forwardIco.invalidate()

    }



    private fun rotationChangeDialog(newConfig: Configuration)
    {
        val alert = android.app.AlertDialog.Builder(this).let {
            it.setTitle("Obracacnie")
            it.setMessage("Jeśli obrucisz ekran wszystkie elementy ostaną zniszczone")
            it.setPositiveButton("tak",DialogInterface.OnClickListener{
                dialog: DialogInterface?, which: Int ->
                //super.onConfigurationChanged(newConfig)
            })
            it.setNegativeButton("nie",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->  })
        }
        alert.create()
    }

    private fun createPalletAlert(){
        val dialog = ColorPalletDialog(this).also {
            it.onPositiveClose = {drawView.paint.color = it.color
                if (menu !=null){
                    setColor(it.color)
                }
                        it.color = drawView.paint.color
                refreshRedoAndUndo()
            }
        }

        dialog.show()
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            dialog.window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setColor(color:Int)
    {

        for (i in 0 until menu!!.size())
        {
            menu!!.getItem(i).icon.setTint(color)
            if(menu!!.getItem(i).hasSubMenu())
                for (j in 0 until menu!!.getItem(i).subMenu.size())
                {
                    menu!!.getItem(i).subMenu.getItem(j).icon.setTint(color)
                }
        }
    }

    private var menu:Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        this.menu = menu
        inflater.inflate(R.menu.app_menu, menu)
        //inflater.inflate(R.menu.symetric_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.symetric_both->
            {
                drawView.symetricType = SymetricType.TwoAxis
                menu?.findItem(R.id.symetric_menu)?.icon = getDrawable(SymetricType.getResource(SymetricType.TwoAxis))
            }
            R.id.symetric_horiontal->
            {
                drawView.symetricType = SymetricType.Horizontal
                menu?.findItem(R.id.symetric_menu)?.icon = getDrawable(SymetricType.getResource(SymetricType.Horizontal))
            }
            R.id.symetric_vertical-> {
                drawView.symetricType = SymetricType.Vertical
                menu?.findItem(R.id.symetric_menu)?.icon = getDrawable(SymetricType.getResource(SymetricType.Vertical))
            }
            R.id.symetric_none->
            {
                drawView.symetricType = SymetricType.None
                menu?.findItem(R.id.symetric_menu)?.icon = getDrawable(SymetricType.getResource(SymetricType.None))
            }
            // drawing type
            R.id.drawType_emptyCircle->{
                drawView.drawElementType = DrawableType.EmptyCircle
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            R.id.drawType_filledCircle->{
                drawView.drawElementType = DrawableType.Circle
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            R.id.drawType_emptyRect->{
                drawView.drawElementType = DrawableType.EmptyRectangle
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            R.id.drawType_filedRect->{
                drawView.drawElementType = DrawableType.Rectangle
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            R.id.drawType_fingerPath->{
                drawView.drawElementType = DrawableType.FingerPath
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            R.id.drawType_line->{
                drawView.drawElementType = DrawableType.Line
                menu?.findItem(R.id.drawType_menu)?.icon = getDrawable(DrawableType.getIcoID(drawView.drawElementType))
            }
            //pallet
            R.id.pallet_menu ->
            {
                createPalletAlert()
            }
            R.id.main_orientation ->{
                val orientation = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT)
                {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                else if (orientation == Configuration.ORIENTATION_LANDSCAPE)
                {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            }


        }
        return super.onOptionsItemSelected(item)
    }




}
