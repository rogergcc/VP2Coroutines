package com.hadi.vp2coroutines.utils

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation


/**
 * Created on febrero.
 * year 2025 .
 */
class PaletteTransformation(private val onPaletteReady: (Palette) -> Unit) : Transformation {
    override fun key() = "paletteTransformer"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val palette = Palette.from(input).generate()
        onPaletteReady(palette)
        return input
    }
}