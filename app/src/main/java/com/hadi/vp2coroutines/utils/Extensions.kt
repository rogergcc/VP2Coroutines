package com.hadi.vp2coroutines.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.viewpager2.widget.ViewPager2
import com.hadi.vp2coroutines.R
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.URL
import kotlin.math.abs

fun Activity.hideSystemUIAndNavigation() {
    val decorView: View = this.window.decorView
    decorView.systemUiVisibility =
        (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
}

@SuppressLint("NewApi")
fun Activity.adjustToolbarMarginForNotch() {
    // Notch is only supported by >= Android 9
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val windowInsets = this.window.decorView.rootWindowInsets
        if (windowInsets != null) {
            val displayCutout = windowInsets.displayCutout
            if (displayCutout != null) {
                val safeInsetTop = displayCutout.safeInsetTop
//                val newLayoutParams = binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
//                newLayoutParams.setMargins(0, safeInsetTop, 0, 0)
//                binding.toolbar.layoutParams = newLayoutParams
            }
        }
    }
}

suspend fun ViewPager2.scrollIndefinitely(interval: Long) {
    delay(interval)
    val numberOfItems = adapter?.itemCount ?: 0
    val lastIndex = if (numberOfItems > 0) numberOfItems - 1 else 0
    val nextItem = if (currentItem == lastIndex) 0 else currentItem + 1

    setCurrentItem(nextItem, true)

    scrollIndefinitely(interval)
}

fun ViewPager2.autoScroll(lifecycleScope: LifecycleCoroutineScope, interval: Long) {
    lifecycleScope.launchWhenResumed {
        scrollIndefinitely(interval)
    }
}

fun URL.toBitmap(): Bitmap? {
    return try {
        BitmapFactory.decodeStream(openStream())
    } catch (e: IOException) {
        null
    }
}
fun ViewPager2.setCarouselEffects(){
    offscreenPageLimit = 1

    val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible).toInt()
    val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
    val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx



    val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = - pageTranslationX * position
        page.scaleY = 1 - (0.25f * abs(position))
        page.alpha = 0.5f + (1 - abs(position))
    }

    // Add a PageTransformer that translates the next and previous items horizontally
    // towards the center of the screen, which makes them visible
//    val nextItemVisiblePx2 = resources.getDimension(R.dimen.viewpager_next_item_visible).toInt()
//    val currentItemHorizontalMarginPx2 = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin).toInt()
//    val pageTranslationX2 = nextItemVisiblePx2 + currentItemHorizontalMarginPx2
//    val pageTransformer2 = ViewPager2.PageTransformer { page: View, position: Float ->
//        page.translationX = -pageTranslationX2 * position
//        // Next line scales the item's height. You can remove it if you don't want this effect
//        page.scaleY = 1 - 0.15f * abs(position)
//    }
//    setPageTransformer(pageTransformer2)

    setPageTransformer(pageTransformer)
}

