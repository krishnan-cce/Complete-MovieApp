package com.example.mvvmovie.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmovie.databinding.PopularRowLayoutBinding
import com.example.mvvmovie.model.movies.Result
import com.example.mvvmovie.ui.OnItemClickListener
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl


class MovieFragmentAdapter(private val clickListener: com.example.mvvmovie.ui.OnItemClickListener): RecyclerView.Adapter<MovieFragmentAdapter.ViewHolder>() {
    private var movieList = ArrayList<Result>()

    class ViewHolder private constructor(private val binding: PopularRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result, clickListener: OnItemClickListener, position: Int) {
            binding.apply {
                ivPopImg.setImageUrl("https://image.tmdb.org/t/p/w500" + item.poster_path)
                tvName.text = item.title
                tvOrgName.text = item.original_title

                itemView.setOnClickListener {
                    clickListener.onItemEditCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PopularRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }
    private fun getItem(position: Int): Result {
        return movieList[position]
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun setData(newData:  List<Result>){
        val moviesDiffUtil =
            DiffUtils(movieList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(moviesDiffUtil)
        if (newData.isEmpty()){
            diffUtilResult.dispatchUpdatesTo(this)

        }else{
            movieList = newData as ArrayList<Result>
            diffUtilResult.dispatchUpdatesTo(this)
        }

    }

}

class OnItemClickListener(val clickListener: (data: Result, pos: Int) -> Unit) {
    fun onItemEditCLick(data: Result, pos: Int) = clickListener(data, pos)
}