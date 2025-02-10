package com.hadi.vp2coroutines.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import coil.load
import coil.size.Scale
import coil.transform.BlurTransformation
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.data.dota2HeroesName
import com.hadi.vp2coroutines.databinding.ActivityUrlImagesBinding
import com.hadi.vp2coroutines.utils.HorizontalMarginItemDecoration
import com.hadi.vp2coroutines.utils.PaletteTransformation
import com.hadi.vp2coroutines.utils.adjustToolbarMarginForNotch
import com.hadi.vp2coroutines.utils.autoScroll
import com.hadi.vp2coroutines.utils.hideSystemUIAndNavigation
import com.hadi.vp2coroutines.utils.setCarouselEffects
import java.util.Locale

class UrlImagesActivity : AppCompatActivity(R.layout.activity_url_images) {

    private lateinit var binding: ActivityUrlImagesBinding
    private lateinit var sliderAdapter: RemoteSliderAdapter
    private lateinit var itemDecoration: HorizontalMarginItemDecoration
    private val INTERVAL_TIME = 5000L

    private var imagesList = mutableListOf<String>()
    private var imageListGenerate = mutableListOf<ImageUrlColorsGenerate>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_url_images)
        binding = ActivityUrlImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sliderAdapter = RemoteSliderAdapter(this@UrlImagesActivity)
        itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.item_decoration
        )
        setupCarouselSlider()

        imagesList = generateNamesFromUrl().toMutableList()

        sliderAdapter.submitList(imagesList)
    }

    private fun generateNamesFromUrl(): List<String> {
        return dota2HeroesName.map {
            val nameParse = it.replace(" ", "_").toLowerCase(Locale.getDefault())
            "https://cdn.cloudflare.steamstatic.com/apps/dota2/videos/dota_react/heroes/renders/${nameParse}.png"
        }
    }

    //region REGION FULL SCREEN WAY
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation()
            adjustToolbarMarginForNotch()
        }
    }
    //endregion

    private fun setupCarouselSlider() {

        binding.viewpager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = sliderAdapter
            setCarouselEffects()
            addItemDecoration(itemDecoration)
            autoScroll(lifecycleScope, INTERVAL_TIME)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    //                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, matchCourseList.size()));
//                    val currentImage = imagesList[position]
//                    binding.imageContainerBlur.load(currentImage) {
//                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))
//                    }
                    val progress =
                        (position + positionOffset) / (binding.viewpager.adapter?.itemCount ?: 1)
//                    progressBar.progress = (progress * 100).toInt()
                    Log.d(TAG, "onPageScrolled progress : $progress")
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    updateBackgroundColor(position)
//                    updateBackgroundColor2(position)
                    val currentImage = imagesList[position]
//                    Log.d("UrlImagesActivity", "imageListGenerate $imageListGenerate")
                    binding.imageContainerBlur.load(currentImage) {
                        transformations(
                            BlurTransformation(context, radius = 24f, sampling = 2f),
                            PaletteTransformation { palette: Palette ->
                                Log.d(TAG, "palete " + palette.vibrantSwatch?.rgb.toString())
                                val swatch = palette.vibrantSwatch
//                                swatch.let{
//                                    binding.containerConstraint.setBackgroundColor(palette.vibrantSwatch?.rgb
//                                        ?: ContextCompat.getColor(
//                                            context,
//                                            R.color.purple_200
//                                        ))
//                                }
                            }
                        )
                        crossfade(true)
                        scale(Scale.FIT)
                        build()
                    }
                }
            })

        }

    }

    private fun updateBackgroundColor(position: Int) {
        if (imageListGenerate.isNotEmpty()) {
            val colorGenerate = imageListGenerate[position].colorGenerate
            binding.containerConstraint.setBackgroundColor(colorGenerate)
        }
    }


    companion object {
        const val TAG = "UrlImagesActivity"
    }
}