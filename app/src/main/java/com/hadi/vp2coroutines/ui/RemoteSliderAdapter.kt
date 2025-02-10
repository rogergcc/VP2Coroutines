package com.hadi.vp2coroutines.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
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


    class SliderViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            binding.ivSlider.load(url) {
                transformations(
                    PaletteTransformation { palette ->
                        val colorVibrant = palette.vibrantSwatch?.rgb ?: ContextCompat.getColor(
                            itemView.context,
                            R.color.purple_200
                        )

                        Log.d(TAG, "palette colorVibrant color: $colorVibrant")
                        itemView.setBackgroundColor(
                            colorVibrant
                        )
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

    companion object {
        private const val TAG = "RemoteSliderAdapter"
    }
}