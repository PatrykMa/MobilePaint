package com.example.patryk.appbarwithdropdownseekbar

import android.content.Context
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.view.ViewGroup
import java.nio.file.Files.size
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.BaseAdapter
import com.example.patryk.mobilepaint.R
import com.example.patryk.mobilepaint.menu_seekbar.MenuNavigationView
import com.example.patryk.mobilepaint.menu_seekbar.SimpleSeekBar


class Adapter {
    private var mListener: SeekBarListener? = null

    interface SeekBarListener {
        fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean, positionInList: Int)
        fun onStartTrackingTouch(seekBar: SeekBar, positionInList: Int)
        fun onStopTrackingTouch(seekBar: SeekBar, positionInList: Int)
    }

    fun getAdapter(context: Context, list: ArrayList<View>, title: String): listAdapter {
        return listAdapter(context, list, title)
    }

    fun setSeekBarListener(listener: SeekBarListener) {
        mListener = listener
    }

    inner class listAdapter(val context: Context, private val itemsList: ArrayList<View>, private val title: String) :
        BaseAdapter() {
        private val mInflater: LayoutInflater
        private var mSeekListener: onSeekbarChange? = null

        init {
            mInflater = LayoutInflater.from(context)
            if (mSeekListener == null) {
                mSeekListener = onSeekbarChange()
            }

        }

        override fun getCount(): Int {
            return itemsList.size
        }

        override fun getItem(position: Int): Any? {
            // TODO Auto-generated method stub
            return null
        }

        override fun getItemId(position: Int): Long {
            // TODO Auto-generated method stub
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            //val holder: ViewHolder2

            if (convertView == null) {
                //holder = ViewHolder2()
                convertView = MenuNavigationView(context).also { it.title = title }
                //convertView
               // holder.text_title = convertView!!.findViewById(R.id.textView)
                //convertView!!.setTag(R.layout.baseadapter_layout, holder)
            } else {
               // holder = convertView!!.getTag(R.layout.baseadapter_layout) as ViewHolder2
            }
            //holder.text_title!!.text = title
            return convertView
        }


        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
           // var convertView = convertView
           // val holder: ViewHolder
           // if (convertView == null) {
           //     holder = ViewHolder()
           //     convertView = SimpleSeekBar(context)
           //     holder.text = convertView!!.findViewById(R.id.textView1)
           //     holder.seekbar = convertView!!.findViewById(R.id.seekBar1)
           //     convertView!!.setTag(R.layout.baseadapter_dropdown_layout, holder)
           // } else {
           //     holder = convertView!!.getTag(R.layout.baseadapter_dropdown_layout) as ViewHolder
           // }
           // holder.text!!.text = itemsList[position]
           // holder.seekbar!!.setOnSeekBarChangeListener(mSeekListener)
           // holder.seekbar!!.tag = position
            return itemsList[position]

        }

    }

    internal class ViewHolder {
        var text: TextView? = null
        var seekbar: SeekBar? = null
    }

    internal class ViewHolder2 {
        var text_title: TextView? = null
    }


    inner class onSeekbarChange : OnSeekBarChangeListener {

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            val position = seekBar.tag as Int
            if (mListener != null) {
                mListener!!.onProgressChanged(seekBar, progress, fromUser, position)
            }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            val position = seekBar.tag as Int
            if (mListener != null) {
                mListener!!.onStartTrackingTouch(seekBar, position)
            }
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            val position = seekBar.tag as Int
            if (mListener != null) {
                mListener!!.onStopTrackingTouch(seekBar, position)
            }
        }

    }
}