package fi.nutrifier.utils

object GraphicsUtils {
    fun valueToWheelDrawn(value: Number, max: Number): Float {
        if (max.toDouble() == 0.0) return 0F;

        val percentage = value.toDouble() / max.toDouble()
        val percentageOfRadius = 360f * percentage
        return percentageOfRadius.toFloat()
    }

    fun valueToLineDrawn(value: Number, max: Number): Float {
        if (max.toDouble() == 0.0) return 0F;

        val percentage = value.toDouble() / max.toDouble()
        val percentageOfLine  = 1f * percentage
        return percentageOfLine.toFloat()
    }
}