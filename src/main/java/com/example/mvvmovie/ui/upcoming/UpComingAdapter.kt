package com.example.mvvmovie.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmovie.databinding.UpcomingRowLayoutBinding
import com.example.mvvmovie.model.upcoming.Upcoming
import com.example.mvvmovie.utils.DiffUtils
import com.example.mvvmovie.utils.setImageUrl


class UpComingAdapter (private val clickListener: OnUpClickListener): RecyclerView.Adapter<UpComingAdapter.ViewHolder>() {
    private var recomendedList = ArrayList<Upcoming>()


    class ViewHolder private constructor(private val binding: UpcomingRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Upcoming, clickListener: OnUpClickListener, position: Int) {
            binding.apply {
                tvTitleUpcoming.text = item.title
                tvTitle2Upcoming.text = item.originalTitle
                ivUpcoming.setImageUrl("https://image.tmdb.org/t/p/w500" + item.posterPath)

                itemView.setOnClickListener {
                    clickListener.onItemCLick(item, position)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UpcomingRowLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    private fun getItem(position: Int): Upcoming {
        return recomendedList[position]
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)

    }

    override fun getItemCount(): Int {
        return recomendedList.size

    }

    fun setData(newData:  List<Upcoming>){
        val recomendedDiffUtil =
            DiffUtils(recomendedList, newData)
        val diffUtilResult = DiffUtil.calculateDiff(recomendedDiffUtil)
        recomendedList = newData as ArrayList<Upcoming>
        diffUtilResult.dispatchUpdatesTo(this)
    }


}

class OnUpClickListener(val clickListener: (data: Upcoming, pos: Int) -> Unit) {
    fun onItemCLick(data: Upcoming, pos: Int) = clickListener(data, pos)
}