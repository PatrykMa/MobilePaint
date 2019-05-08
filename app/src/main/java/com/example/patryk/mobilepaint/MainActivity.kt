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
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Environment
import android.provider.MediaStore
import android.widget.EditText
import android.widget.SeekBar
import androidx.appcompat.app.ActionBar

import androidx.core.app.ActivityCompat
import androidx.core.graphics.ColorUtils
import com.example.patryk.appbarwithdropdownseekbar.Adapter
import com.example.patryk.mobilepaint.menu_seekbar.SimpleSeekBar
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class MainActivity : AppCompatActivity() {

    lateinit private var drawView:DrawView
    private val PICK_IMAGE = 2
    private val PERMISSIONS = 3

    // The following are used for the shake detection
	private lateinit var mSensorManager: SensorManager
	private lateinit var mAccelerometer: Sensor
	private lateinit var mShakeDetector:ShakeDetector

    private lateinit var menuSeekbar:SimpleSeekBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawView = findViewById<DrawView>(R.id.drawView)


        //menuSeekbar = SimpleSeekBar(this)
        //menuSeekbar.onValueChange={
        //    if(menuSeekbar.progresValue != null)
        //        drawView.paint.strokeWidth = menuSeekbar.progresValue!!.toFloat()}
        //menuSeekbar.progresValue = drawView.paint.strokeWidth.toInt()
        //val adapter = Adapter()
        //supportActionBar?.navigationMode = ActionBar.NAVIGATION_MODE_LIST
        //supportActionBar?.setListNavigationCallbacks(adapter.getAdapter(this, arrayListOf(menuSeekbar),"Rozmiar"), this);
        getSupportActionBar()?.setDisplayShowCustomEnabled(true)
        getSupportActionBar()?.setCustomView(R.layout.app_bar)
        setOnClickListenerToAppBarViews()
        refreshRedoAndUndo()

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mShakeDetector =ShakeDetector()
		mShakeDetector.mListener = {count: Int ->
            createClearDialog()
        }

    }





    private fun createSizeDialog(){

        val seekBar = SeekBar(this)
        seekBar.progress = drawView.paint.strokeWidth.toInt()
        val alert = AlertDialog.Builder(this).let {
            it.setTitle("Rozmiar pędzla")
            it.setMessage("Ustaw")
            it.setView(seekBar)
            it.setPositiveButton("Zapisz",DialogInterface.OnClickListener{
                    dialog: DialogInterface?, which: Int ->
                drawView.paint.strokeWidth = seekBar.progress.toFloat()
            }).create()
            // it.setNegativeButton("Anuluj",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->  })
        }
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        }
        alert.show()
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

    public override fun onResume() {
        super.onResume()
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI)

    }

    public override fun onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector)
        super.onPause()
    }

    private fun setColor(color:Int)
    {
        setAppBarColor(color)
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

    private fun setAppBarColor(colorTochange:Int)
    {
        val bar = getSupportActionBar()
        var colorToSet = Color.RED

        val red = Color.red(colorTochange)
        val green = Color.green(colorTochange)
        val blue = Color.blue(colorTochange)

        val NEGATIVE = floatArrayOf(
            -1.0f, 0f, 0f, 0f, 255f, // red
            0f, -1.0f, 0f, 0f, 255f, // green
            0f, 0f, -1.0f, 0f, 255f, // blue
            0f, 0f, 0f, 1.0f, 0f  // alpha
        )

        if( red == Math.max(red, green) && red == Math.max(red, blue) )
            colorToSet = Color.BLUE
//
        if( blue == Math.max(blue, green) && blue == Math.max(red, blue) )
            colorToSet = Color.GREEN
//
        if( green == Math.max(red, green) && green == Math.max(green, blue) )
            colorToSet = Color.RED

        bar?.setBackgroundDrawable(ColorDrawable(colorTochange).also { it.setColorFilter(ColorMatrixColorFilter(NEGATIVE))})

        //val newRed = (0.393 * red + 0.769 * green + 0.189 * blue).toInt()
        //val newGreen = (0.349 * red + 0.686 * green + 0.168 * green).toInt()
        //val newBlue = (0.272 * red + 0.534 * green + 0.131 * green).toInt()
        //val sepia = Color.rgb(newRed, newGreen, newBlue)
        //bar?.setBackgroundDrawable(ColorDrawable(sepia))
    }

    private fun createClearDialog(){

        val alert = AlertDialog.Builder(this).let {
            it.setTitle("Czy na pewno chcesz wyczyści widok")


            it.setPositiveButton("Tak",DialogInterface.OnClickListener{
                    dialog: DialogInterface?, which: Int ->
                drawView.clear()
            }).create()
            // it.setNegativeButton("Anuluj",DialogInterface.OnClickListener{dialog: DialogInterface?, which: Int ->  })
        }
        alert.setOnShowListener {
            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE)
        }
        alert.show()
    }

    private var menu:Menu? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        this.menu = menu
        inflater.inflate(R.menu.app_menu, menu)
        //inflater.inflate(R.menu.symetric_menu, menu)
        val r=super.onCreateOptionsMenu(menu)
        setColor(drawView.paint.color)
        return r

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
            R.id.size ->{
                createSizeDialog()
            }
            //pallet
            R.id.main_clear -> {
                createClearDialog()
            }

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
