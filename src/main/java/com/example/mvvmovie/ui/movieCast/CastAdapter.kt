package com.example.mvvmovie.ui.movieCast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmovie.databinding.CardCastLayoutBinding
import com.example.mvvmovie.databinding.CastRowLayoutBinding
import com.example.mvvmovie.databinding.VideosRowLayoutBinding
import com.example.mvvmovie.model.casts.Cast
import com.example.mvvmovie.model.videos.Videos
import com.example.mvvmovie.ui.movieVideos.OnVideoClickListener
import com.example.mvvmovie.ui.movieVideos.VideoAdapter
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl

class CastAdapter( private val clickListener: OnCastClickListener): RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    private var castList = ArrayList<Cast>()


    class ViewHolder private constructor(private val binding: CastRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Cast, clickListener: OnCastClickListener, position: Int) {
            binding.apply {

                ivProfilepath.setImageUrl("https://image.tmdb.org/t/p/w500" + item.profilePath)
                tvName.text = item.originalName
                tvCastingName.text = item.character
                itemView.setOnClickListener {
                    clickListener.onItemCastCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CastRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }
    private fun getItem(position: Int): Cast {

        return castList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }

    override fun getItemCount(): Int {
        return castList.size
    }


    fun setData(newData:  List<Cast>){
        val castDiffUtil = DiffUtils(castList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(castDiffUtil)

        castList = newData as ArrayList<Cast>

        diffUtilResult.dispatchUpdatesTo(this)
    }


}

class OnCastClickListener(val clickListener: (data: Cast, pos: Int) -> Unit) {
    fun onItemCastCLick(data: Cast, pos: Int) = clickListener(data, pos)
}