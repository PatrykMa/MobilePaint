package com.example.patryk.mobilepaint

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
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
            createPalletAlert()
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

    private fun createPalletAlert(){
        val dialog = ColorPalletDialog(this).also {
            it.onPositiveClose = {drawView.paint.color = it.color
                if (menu !=null){
                    setColor(it.color)
                }
                        it.color = drawView.paint.color
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
        val drawMenu = (menu?.findItem(R.id.drawType_menuElem) as Menu?)
        if(drawMenu != null)
        for (i in 0 until drawMenu.size())
        {
            drawMenu.getItem(i).icon.setTint(color)
        }
        for (i in 0 until menu!!.size())
        {
            menu!!.getItem(i).icon.setTint(color)
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


        }
        return super.onOptionsItemSelected(item)
    }




}
