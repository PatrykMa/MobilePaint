package com.example.patryk.mobilepaint.drawable

import android.graphics.Paint
import com.example.patryk.mobilepaint.drawable.elements.*

class DrawableFactory {
    companion object {

        fun createObject(paint: Paint, type: DrawableType): Drawable {
            return when (type) {
                DrawableType.Line -> {
                    makeLine(paint)
                }
                DrawableType.FingerPath -> {
                    makeFingerPath(paint)
                }
                DrawableType.Circle -> {
                    makeCircle(paint)
                }
                DrawableType.EmptyCircle-> {
                    makeEmptyCirlcle(paint)
                }
                DrawableType.EmptyRectangle-> {
                    makeEmptyrectangle(paint)
                }
                DrawableType.Rectangle->
                    makeRectangle(paint)
            }
        }

        fun makeCircle(paint: Paint): Circle {
            return Circle(paint)
        }

        fun makeLine(paint: Paint): Line {
            return Line(paint)
        }

        fun makeFingerPath(paint: Paint): FingerPath {
            return FingerPath(paint)
        }
        fun makeEmptyCirlcle(paint: Paint):EmptyCircle
        {
            return EmptyCircle(paint)
        }
        fun makeRectangle(paint: Paint):Rectangle
        {
            return Rectangle(paint)
        }

        fun makeEmptyrectangle(paint: Paint):EmptyRectangle
        {
            return EmptyRectangle(paint)
        }
    }
}