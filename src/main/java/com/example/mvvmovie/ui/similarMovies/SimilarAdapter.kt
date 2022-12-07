package com.example.mvvmovie.ui.similarMovies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmovie.model.similar.Result
import com.example.mvvmovie.databinding.SimilarRowLayoutBinding
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl

class SimilarAdapter( private val clickListener: OnItemClickListener)
    : RecyclerView.Adapter<SimilarAdapter.ViewHolder>()  {
    private var similarList = ArrayList<Result>()


    class ViewHolder private constructor(private val binding: SimilarRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result, clickListener: OnItemClickListener, position: Int) {
            binding.apply {
                tvName.text = item.title
                tvCastingName.text = item.originalTitle
                ivProfilepath.setImageUrl("https://image.tmdb.org/t/p/w500" + item.posterPath)

                itemView.setOnClickListener {
                    clickListener.onItemEditCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SimilarRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ViewHolder {
        return ViewHolder.from(parent)
    }


    private fun getItem(position: Int): Result {
        return similarList[position]
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }

    override fun getItemCount(): Int {
        return similarList.size
    }

    fun setData(newData:  List<Result>){
        val similarDiffUtil =
            DiffUtils(similarList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(similarDiffUtil)
        similarList = newData as ArrayList<Result>
        diffUtilResult.dispatchUpdatesTo(this)
    }

}

class OnItemClickListener(val clickListener: (data: Result, pos: Int) -> Unit) {
    fun onItemEditCLick(data: Result, pos: Int) = clickListener(data, pos)
}