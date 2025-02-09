package com.hadi.vp2coroutines.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.BlurTransformation
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.databinding.ActivityMainBinding
import com.hadi.vp2coroutines.utils.HorizontalMarginItemDecoration
import com.hadi.vp2coroutines.utils.autoScroll
import com.hadi.vp2coroutines.utils.setCarouselEffects
import kotlinx.coroutines.launch


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
            hideSystemUIAndNavigation(this)
            adjustToolbarMarginForNotch()
        }
    }

    private fun hideSystemUIAndNavigation(activity: Activity) {
        val decorView: View = activity.window.decorView
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
    private fun adjustToolbarMarginForNotch() {
        // Notch is only supported by >= Android 9
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val windowInsets = window.decorView.rootWindowInsets
            if (windowInsets != null) {
                val displayCutout = windowInsets.displayCutout
                if (displayCutout != null) {
                    val safeInsetTop = displayCutout.safeInsetTop
                    val newLayoutParams =
                        binding.toolbar.layoutParams as ViewGroup.MarginLayoutParams
                    newLayoutParams.setMargins(0, safeInsetTop, 0, 0)
                    binding.toolbar.layoutParams = newLayoutParams
                }
            }
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

    data class ImageData(val id: Int, @DrawableRes val imageResource: Int)

    data class ImageColorsGenerate(@DrawableRes val imageResGenerate: Int, val colorGenerate: Int)


    fun ImageData.toGenerateColorsDrawableImage(): ImageColorsGenerate {
        val imageSelected = BitmapFactory.decodeResource(
            resources,
            imageResource
        )
        //from url

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

    fun String.removeFirstLastChar(): String = this.substring(1, this.length - 1)

    fun String.toGenerateColorsURLImage(): ImageUrlColorsGenerate {
        var colorGenerate = 0
        lifecycleScope.launch {
            val loader = ImageLoader(baseContext)
            val request = ImageRequest.Builder(baseContext)
                .data(this)
                .allowHardware(false) // Disable hardware bitmaps.
                .build()

            val result = (loader.execute(request) as SuccessResult).drawable
            val imageSelected = (result as BitmapDrawable).bitmap
            val color = createPaletteSync(imageSelected).vibrantSwatch

            colorGenerate = (color?.rgb ?: ContextCompat.getColor(
                baseContext,
                R.color.purple_200
            ))
        }

//        val loader = ImageLoader(context = baseContext)
//        val req = ImageRequest.Builder(baseContext)
//            .data(this)
//            .target { result ->
//                val bitmap = (result as BitmapDrawable).bitmap
//                val color= createPaletteSync(bitmap).vibrantSwatch
//
//                val colorGenerate= (color?.rgb ?: ContextCompat.getColor(
//                    baseContext,
//                    R.color.purple_200
//                ))
//            }
//            .build()
//        val disposable = loader.enqueue(req)


        return ImageUrlColorsGenerate(
            imageUrl = this,
            colorGenerate = colorGenerate

        )
    }


    data class ImageUrlColorsGenerate(val imageUrl: String, val colorGenerate: Int)


    private fun setupData() {

        //GET LIST WITH COLORS
        imageListGenerate = getListWithColors().toMutableList()

//        questionListingsDto.map { it.toQuestionListing() }
//        imagesList.addAll(links.map { imageData -> imageData })
        imagesList.addAll(imageDataList.map { imageData -> imageData.imageResource.toString() })

//        imagesList.add("https://cdn.pixabay.com/photo/2020/12/10/09/22/beach-front-5819728_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2020/09/03/13/56/pine-5541335_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/04/15/29/river-6068374_960_720.jpg")
//        imagesList.add("https://cdn.pixabay.com/photo/2021/03/29/08/22/peach-flower-6133330_960_720.jpg")
    }

    private fun getListWithColors() = imageDataList.map { imageData ->
        (imageData.toGenerateColorsDrawableImage())

    }


}
