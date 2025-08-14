package fi.nutrifier.utils

object GraphicsUtils {
    fun valueToWheelDrawn(value: Number, max: Number): Float {
        val percentage = value.toDouble() / max.toDouble()
        val percentageOfRadius = 360f * percentage
        return percentageOfRadius.toFloat()
    }
}