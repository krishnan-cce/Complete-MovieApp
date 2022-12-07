package com.example.mvvmovie.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmovie.databinding.MovieLayoutBinding
import com.example.mvvmovie.model.movies.Result
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl


class MovieAdapter(private val clickListener: OnItemClickListener): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {
    private var movieList = ArrayList<Result>()

    class ViewHolder private constructor(private val binding: MovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result, clickListener: OnItemClickListener, position: Int) {
            binding.apply {
                movieImage.setImageUrl("https://image.tmdb.org/t/p/w500" + item.poster_path)
                movieName.text = item.title

                itemView.setOnClickListener {
                    clickListener.onItemEditCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieLayoutBinding.inflate(layoutInflater, parent, false)
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