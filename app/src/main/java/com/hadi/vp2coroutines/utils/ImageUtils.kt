package com.hadi.vp2coroutines.utils


/**
 * Created on febrero.
 * year 2025 .
 */

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.ui.ImageUrlColorsGenerate
import com.hadi.vp2coroutines.ui.UrlImagesActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

private const val TAG = "ImageUtils"
suspend fun uriToBitmap(context: Context, uri: Uri?): Bitmap {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(uri)
        .allowHardware(false)
        .build()
    val result = (loader.execute(request) as SuccessResult).drawable

//    val bitmap = (result as BitmapDrawable).bitmap
//    val resizedBitmap = Bitmap.createScaledBitmap(
//        bitmap, 80, 80, true
//    );


    return (result as BitmapDrawable).bitmap
}

fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

suspend fun String.getColorsFromUrlImage(context: Context): ImageUrlColorsGenerate {
    val bitmap = uriToBitmap(context, Uri.parse(this))
    val palette = createPaletteSync(bitmap)
    val color = palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(context, R.color.purple_200)
    return ImageUrlColorsGenerate(imageUrl = this, colorGenerate = color)
}

suspend fun getBitmap(context: Context, url: String): Bitmap? {
    var bitmap: Bitmap? = null
    val request = ImageRequest.Builder(context)
        .data(url)
        .transformations(
            PaletteTransformation { palette ->
                val swatch = palette.vibrantSwatch
                Log.d(TAG, palette.vibrantSwatch?.rgb.toString())
            }
        )
        .crossfade(true)
        .scale(coil.size.Scale.FIT)
        .build()

    val loader = ImageLoader(context)
    val result = (loader.execute(request) as? coil.request.SuccessResult)?.drawable
    bitmap = (result as? android.graphics.drawable.BitmapDrawable)?.bitmap

    return bitmap
}
// Usage example
//CoroutineScope(Dispatchers.IO).launch {
//    val bitmap = getBitmap(this@UrlImagesActivity, "https://example.com/image.png")
//    withContext(Dispatchers.Main) {
//        // Use the bitmap on the main thread
//    }
//}


suspend fun String.toGenerateColorsURLImage(context: Context): ImageUrlColorsGenerate {
    val bitmap = getBitmap(context, this)
    val colorGenerate = bitmap?.let {
        val palette = createPaletteSync(it)
        palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(context, R.color.purple_200)
    } ?: ContextCompat.getColor(context, R.color.purple_200)

    return ImageUrlColorsGenerate(
        imageUrl = this,
        colorGenerate = colorGenerate
    )
}
//CoroutineScope(Dispatchers.IO).launch {
//    val imageUrlColorsGenerate = "https://example.com/image.png".toGenerateColorsURLImage(this@UrlImagesActivity)
//    withContext(Dispatchers.Main) {
//        binding.containerConstraint.setBackgroundColor(imageUrlColorsGenerate.colorGenerate)
//    }
//}


//private fun generateNewListWithColors() {
//    Log.d(UrlImagesActivity.TAG, "M=>$imagesList")
//    imagesList.forEach { imageData ->
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val imageUrlColorsGenerate = imageData.toGenerateColorsURLImage(baseContext)
//                withContext(Dispatchers.Main) {
//                    imageListGenerate.add(imageUrlColorsGenerate)
//                    Log.d(UrlImagesActivity.TAG, "O=>$imageUrlColorsGenerate")
//                }
//            } catch (e: IOException) {
//                Log.e("Error: ", e.message.toString())
//            }
//        }
//    }
//}
