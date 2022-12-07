package com.example.mvvmovie.ui.movieRecomended

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.example.mvvmovie.databinding.RecomendedMovieLayoutBinding
import com.example.mvvmovie.model.recomendations.Recomendations
import com.example.mvvmovie.model.similar.Result
import com.example.mvvmovie.ui.similarMovies.SimilarAdapter
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl

class RecomendedAdapter(private val clickListener: OnRecClickListener): RecyclerView.Adapter<RecomendedAdapter.ViewHolder>() {
    private var recomendedList = ArrayList<Recomendations>()


    class ViewHolder private constructor(private val binding: RecomendedMovieLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recomendations, clickListener: OnRecClickListener, position: Int) {
            binding.apply {
                tvName.text = item.title
                tvCastingName.text = item.originalTitle
                ivProfilepath.setImageUrl("https://image.tmdb.org/t/p/w500" + item.posterPath)

                itemView.setOnClickListener {
                    clickListener.onItemCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecomendedMovieLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    private fun getItem(position: Int): Recomendations {
        return recomendedList[position]
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)

    }

    override fun getItemCount(): Int {
        return recomendedList.size

    }

    fun setData(newData:  List<Recomendations>){
        val recomendedDiffUtil =
            DiffUtils(recomendedList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recomendedDiffUtil)
        recomendedList = newData as ArrayList<Recomendations>
        diffUtilResult.dispatchUpdatesTo(this)
    }


}

class OnRecClickListener(val clickListener: (data: Recomendations, pos: Int) -> Unit) {
    fun onItemCLick(data: Recomendations, pos: Int) = clickListener(data, pos)
}