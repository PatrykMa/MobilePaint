package com.example.patryk.mobilepaint.drawable.symetry

import com.example.patryk.mobilepaint.R

enum class SymetricType {
    None,
    Horizontal,
    Vertical,
    TwoAxis,
    FourAxis;
    companion object {
        fun getResource(type:SymetricType):Int{
            return when(type)
            {
                None -> R.drawable.icons8_symetric_none_24
                Horizontal -> R.drawable.icons8_symetric_horizontal_24
                Vertical -> R.drawable.icons8_symetric_vertical_24
                TwoAxis -> R.drawable.icons8_symetric_both_24
                else -> R.drawable.icons8_symetric_none_24
            }
        }
    }
}