package com.hadi.vp2coroutines.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.load
import coil.transform.BlurTransformation
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.databinding.ActivityMainBinding
import com.hadi.vp2coroutines.ui.cache.SliderAdapter
import com.hadi.vp2coroutines.ui.models.ImageColorsGenerate
import com.hadi.vp2coroutines.ui.models.ImageData
import com.hadi.vp2coroutines.utils.HorizontalMarginItemDecoration
import com.hadi.vp2coroutines.utils.adjustToolbarMarginForNotch
import com.hadi.vp2coroutines.utils.autoScroll
import com.hadi.vp2coroutines.utils.hideSystemUIAndNavigation
import com.hadi.vp2coroutines.utils.setCarouselEffects


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val INTERVAL_TIME = 5000L
    private var imagesList = mutableListOf<String>()
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var itemDecoration: HorizontalMarginItemDecoration
    private var imageListGenerate = mutableListOf<ImageColorsGenerate>()
    val imageDataList = listOf(
        ImageData(1, R.drawable.clash_of_clans_archer),
        ImageData(2, R.drawable.pngwing_3),
        ImageData(3, R.drawable.pngwing_1),
        ImageData(4, R.drawable.pngwing_4),
        ImageData(5, R.drawable.pngwing_5),
        ImageData(6, R.drawable.pngwing_6),
        ImageData(7, R.drawable.pngwing_7),
        ImageData(8, R.drawable.pngwing_8),
        ImageData(9, R.drawable.pngwing_9),
        ImageData(10, R.drawable.pngwing_10),
        ImageData(11, R.drawable.pngwing_11),
        ImageData(12, R.drawable.pngwing_12),
        ImageData(13, R.drawable.pngwing_13),
        ImageData(14, R.drawable.pngwing_14)
    )

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUIAndNavigation()
            adjustToolbarMarginForNotch()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sliderAdapter = SliderAdapter(this@MainActivity)
        itemDecoration = HorizontalMarginItemDecoration(
            this,
            R.dimen.item_decoration
        )


        setupCarouselSlider()

//        setupNormalSlider()
        setupData()
        setupAdapter()
        //binding.containerConstraint.setBackgroundResource(R.drawable.clash_of_clans_archer)

    }

//    private fun setupNormalSlider() {
//        binding.viewpagerNormal.apply {
//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
//            adapter = sliderAdapter
//            addItemDecoration(itemDecoration)
//            autoScroll(lifecycleScope, INTERVAL_TIME)
//        }
//    }

    private fun setupCarouselSlider() {
        binding.viewpager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = sliderAdapter
            setCarouselEffects()
            addItemDecoration(itemDecoration)
            autoScroll(lifecycleScope, INTERVAL_TIME)
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int,
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    //                countTxtView.setText(String.format(Locale.ENGLISH,"%d/%d", position+1, matchCourseList.size()));

                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

//                    val color = (Math.random() * 16777215).toInt() or (0xFF shl 24)
                    //set Rand Color
//                    binding.containerConstraint.setBackgroundColor(color)


//                    val currentImage = imagesList.get(position)
                    val currentImage = imagesList[position].toInt()

//                    binding.containerConstraint.setBackgroundResource(currentImage)

                    //region COLOR GENER M 1
                    //color from Image
//                    val imageSelected = BitmapFactory.decodeResource(
//                        resources,
//                        currentImage
//                    )
//                    setToolbarColor(imageSelected)
                    //endregion

                    //region COLOR GENER M 2
//                    createPaletteAsync(
//                        (ContextCompat.getDrawable(
//                            context,
//                            currentImage
//                        ) as BitmapDrawable).bitmap
//                    )
                    //endregion


                    val colorGenerate = imageListGenerate[position].colorGenerate

                    binding.containerConstraint.setBackgroundColor(colorGenerate)

                    binding.imageContainerBlur.load(currentImage) {

                        transformations(BlurTransformation(context, radius = 24f, sampling = 2f))
                    }
                }
            })

        }


    }

    // Generate palette synchronously and return it
    fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()


    fun createPaletteAsync(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            binding.containerConstraint.apply {
                setBackgroundColor(
                    (palette?.vibrantSwatch?.rgb ?: ContextCompat.getColor(
                        baseContext,
                        R.color.purple_200
                    ))
                )
            }


        }
    }

    private fun setToolbarColor(bitmap: Bitmap) {
        // Generate the palette and get the vibrant swatch
        val vibrantSwatch = createPaletteSync(bitmap).vibrantSwatch
        val vibrantSwatch_2 = createPaletteSync(bitmap).darkVibrantSwatch

        binding.containerConstraint.setBackgroundColor(
            (vibrantSwatch?.rgb ?: ContextCompat.getColor(
                baseContext,
                R.color.purple_200
            ))
        )
    }

    private fun setupAdapter() {
        sliderAdapter.setImages(imagesList)
        sliderAdapter.notifyDataSetChanged()
    }


    fun ImageData.toGenerateColorsDrawableImage(): ImageColorsGenerate {
        val imageSelected = BitmapFactory.decodeResource(
            resources,
            imageResource
        )

//        val imageSelectedURl = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageResource+"")

        val color = createPaletteSync(imageSelected).vibrantSwatch

        val colorGenerate = (color?.rgb ?: ContextCompat.getColor(
            baseContext,
            R.color.purple_200
        ))

        return ImageColorsGenerate(

            imageResGenerate = imageResource,
            colorGenerate = colorGenerate

        )
    }



    private fun setupData() {
        imageListGenerate = getListWithColors().toMutableList()

//        questionListingsDto.map { it.toQuestionListing() }
//        imagesList.addAll(links.map { imageData -> imageData })
        imagesList.addAll(imageDataList.map { imageData -> imageData.imageResource.toString() })

    }

    private fun getListWithColors() = imageDataList.map { imageData ->
        (imageData.toGenerateColorsDrawableImage())

    }


}
