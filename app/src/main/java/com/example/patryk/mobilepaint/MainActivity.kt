package com.example.patryk.mobilepaint


import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView

import com.example.patryk.mobilepaint.drawable.DrawableType
import com.example.patryk.mobilepaint.drawable.symetry.SymetricType
import android.content.Intent
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import androidx.appcompat.app.ActionBar

import androidx.core.app.ActivityCompat
import androidx.core.graphics.ColorUtils
import com.example.patryk.appbarwithdropdownseekbar.Adapter
import com.example.patryk.mobilepaint.menu_seekbar.SimpleSeekBar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity(), ActionBar.OnNavigationListener {

    lateinit private var drawView:DrawView
    private val PICK_IMAGE = 2
    private val PERMISSIONS = 3

    private lateinit var menuSeekbar:SimpleSeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setCustomView(R.layout.app_bar)
        actionBar
        drawView = findViewById<DrawView>(R.id.drawView)
        setOnClickListenerToAppBarViews()
        refreshRedoAndUndo()
        menuSeekbar = SimpleSeekBar(this)
        menuSeekbar.onValueChange={
            if(menuSeekbar.progresValue != null)
                drawView.paint.strokeWidth = menuSeekbar.progresValue!!.toFloat()}
        menuSeekbar.progresValue = drawView.paint.strokeWidth.toInt()
        val adapter = Adapter()
        supportActionBar?.navigationMode = ActionBar.NAVIGATION_MODE_LIST
        supportActionBar?.setListNavigationCallbacks(adapter.getAdapter(this, arrayListOf(menuSeekbar),"Rozmiar"), this);

    }

    override fun onNavigationItemSelected(itemPosition: Int, itemId: Long): Boolean {
       // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return false
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
            dialog.window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    private fun setColor(color:Int)
    {
        //val colorWithOutAlfa = ColorUtils.setAlphaComponent(color, 0)
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

    private fun selectImage()
    {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent, PICK_IMAGE)
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
                changeOrientation()
            }
            R.id.main_load ->{
                selectImage()
            }
            R.id.main_save ->{
                if (hasPermission()) {
                    val input = EditText(this)
                    val alert = AlertDialog.Builder(this).let {
                        it.setTitle("Nazwa")
                        it.setMessage("Podaj Nazwe pliku")
                        it.setView(input)
                        it.setPositiveButton("Zapisz",DialogInterface.OnClickListener{
                                dialog: DialogInterface?, which: Int ->
                                saveBitmap(input.text.toString())
                        }).create()
                       // it.setNegativeButton("Anuluj",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->  })
                    }
                    alert.setOnShowListener {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
                    }
                    alert.show()
                   //saveBitmap()
                }
                else askPermsion()
            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun changeOrientation()
    {
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

    private fun hasPermission():Boolean
    {
        return ( checkSelfPermission(  android.Manifest.permission.READ_EXTERNAL_STORAGE  ) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(  android.Manifest.permission.WRITE_EXTERNAL_STORAGE  ) == PackageManager.PERMISSION_GRANTED)
    }
    private fun askPermsion()
    {
        ActivityCompat.requestPermissions( this, arrayOf(  android.Manifest.permission.READ_EXTERNAL_STORAGE ,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE),PERMISSIONS)
    }


    private fun saveBitmap(name:String)
    {
        val path = Environment.getExternalStorageDirectory().toString() + "/" +  Environment.DIRECTORY_DCIM
        var fOut: OutputStream? = null
        val counter = 0
        val file = File(
            path,
            "$name.png"
        ) // the File to save , append increasing numeric counter to prevent files from getting overwritten.
        if (file.exists())
            file.delete()
        fOut = FileOutputStream(file)

        val pictureBitmap = drawView.getAsBitmap // obtaining the Bitmap
        pictureBitmap.config = Bitmap.Config.ARGB_8888
        pictureBitmap.compress(
            Bitmap.CompressFormat.PNG,
            100,
            fOut
        ) // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
        fOut.flush() // Not really required
        fOut.close() // do not forget to close the stream

        MediaStore.Images.Media.insertImage(contentResolver, file.getAbsolutePath(), file.getName(), file.getName())
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE && data != null) {
            val inputStream = contentResolver.openInputStream(data.data)
            val bmp:Bitmap = BitmapFactory.decodeStream(inputStream)
            drawView.backgroundBitmap = bmp
            if(bmp.width > bmp.height && resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                changeOrientation()
            if(bmp.width < bmp.height && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                changeOrientation()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }




}
