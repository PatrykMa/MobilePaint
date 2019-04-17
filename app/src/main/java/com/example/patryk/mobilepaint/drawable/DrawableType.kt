package com.example.patryk.mobilepaint.drawable

import com.example.patryk.mobilepaint.R

enum class DrawableType {
    Circle,
    FingerPath,
    Line,
    EmptyCircle,
    Rectangle,
    EmptyRectangle;
    companion object {
        fun getIcoID(type:DrawableType):Int{
            return when(type){
                Circle -> R.drawable.icons8_filled_circle_24
                FingerPath-> R.drawable.icons8_edit_24
                Line -> R.drawable.icons8_line_24
                EmptyCircle -> R.drawable.icons8_circle_24
                Rectangle -> R.drawable.icons8_filled_rectangular_24
                EmptyRectangle -> R.drawable.icons8_rectangular_24
            }
        }
    }
}