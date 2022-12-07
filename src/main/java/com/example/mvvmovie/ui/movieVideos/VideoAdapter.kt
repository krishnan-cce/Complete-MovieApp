package com.example.mvvmovie.ui.movieVideos


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmovie.databinding.SimilarRowLayoutBinding
import com.example.mvvmovie.databinding.VideosRowLayoutBinding
import com.example.mvvmovie.model.similar.Result
import com.example.mvvmovie.model.videos.Videos

import com.example.mvvmovie.ui.similarMovies.SimilarAdapter
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.hide
import com.example.mvvmovie.utils.setImageUrl


class VideoAdapter( private val clickListener: OnVideoClickListener): RecyclerView.Adapter<VideoAdapter.ViewHolder>() {
    private var videoList = ArrayList<Videos>()

    class ViewHolder private constructor(private val binding: VideosRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Videos, clickListener: OnVideoClickListener, position: Int) {
            binding.apply {
                ivThumbnail.setImageUrl("https://i.ytimg.com/vi/" + item.key + "/0.jpg")

                itemView.setOnClickListener {
                    clickListener.onItemVideoCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = VideosRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    private fun getItem(position: Int): Videos {
        return videoList[position]
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)

    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    fun setData(newData:  List<Videos>){
        val videoDiffUtil =
            DiffUtils(videoList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(videoDiffUtil)
        videoList = newData as ArrayList<Videos>
        diffUtilResult.dispatchUpdatesTo(this)
    }



}
class OnVideoClickListener(val clickListener: (data: Videos, pos: Int) -> Unit) {
    fun onItemVideoCLick(data: Videos, pos: Int) = clickListener(data, pos)
}