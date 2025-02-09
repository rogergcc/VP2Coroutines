package com.hadi.vp2coroutines.ui

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import coil.bitmap.BitmapPool
import coil.load
import coil.size.Scale
import coil.size.Size
import coil.transform.Transformation
import com.hadi.vp2coroutines.R
import com.hadi.vp2coroutines.databinding.ItemSliderBinding
import com.hadi.vp2coroutines.utils.PaletteTransformation

class RemoteSliderAdapter(
    private val context: Context,
) : RecyclerView.Adapter<RemoteSliderAdapter.SliderViewHolder>() {

//    private var mImageList = mutableListOf<String>()

    private var mImageList = emptyList<String>()
    fun submitList(list: List<String>) {
        mImageList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
//        holder.bind(list[position].toInt())
        holder.bind(mImageList[position])
    }

    override fun getItemCount() = mImageList.size

    // onItemClickListener
    private var onItemClickListener: ((Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    class SliderViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            binding.ivSlider.load(url) {
                transformations(
                    PaletteTransformation { palette ->
                        val swatch = palette.vibrantSwatch
                        Log.d("RemoteAdapter", palette.vibrantSwatch?.rgb.toString())
                        if (swatch != null) {
                            itemView.setBackgroundColor(
                                palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
                                    itemView.context,
                                    R.color.purple_200
                                )
                            )
                        }
                    }
                )
                crossfade(true)
//                transformations(RoundedCornersTransformation(24f))
                scale(Scale.FIT)
                build()
//                crossfade(750)
//                placeholder(errorPlaceHolder)
//                transformations(CircleCropTransformation())
//                transformations(RoundedCornersTransformation(2f))
//                error(errorPlaceHolder)

            }
        }
    }
}