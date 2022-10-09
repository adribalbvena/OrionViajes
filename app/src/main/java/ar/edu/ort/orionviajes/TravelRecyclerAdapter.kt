package ar.edu.ort.orionviajes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.databinding.ItemTravelBinding


class TravelRecyclerAdapter: ListAdapter<TravelX, TravelRecyclerAdapter.TravelViewHolder>(TravelDiffutilCallback()) {

    class TravelViewHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root)

    class TravelDiffutilCallback: DiffUtil.ItemCallback<TravelX>(){
        override fun areItemsTheSame(oldItem: TravelX, newItem: TravelX): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TravelX, newItem: TravelX): Boolean {
            return oldItem === newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val inflater = ContextCompat.getSystemService(parent.context, LayoutInflater::class.java) as LayoutInflater
        val binding = ItemTravelBinding.inflate(inflater, parent, false)
        return TravelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvTravelTitle.text = item.title
        holder.binding.tvTravelBudget.text = item.budget.toString()
        holder.binding.tvTravelStartDate.text = item.startDate
        holder.binding.tvTravelEndDate.text = item.endDate
    }
}