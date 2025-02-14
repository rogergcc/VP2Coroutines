package com.hadi.vp2coroutines.ui.cache

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.hadi.vp2coroutines.databinding.ItemSliderBinding

class SliderAdapter(
    private val context: Context
) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private var list = mutableListOf<String>()


    fun setImages(list: List<String>){
        this.list.clear()
        this.list.addAll(list);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = ItemSliderBinding.inflate(LayoutInflater.from(context), parent, false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
//        holder.bind(list[position].toInt())
        holder.bind(list[position].toInt())
    }

    override fun getItemCount() = list.size

    class SliderViewHolder(val binding: ItemSliderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: Int) {
            binding.ivSlider.load(url){
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