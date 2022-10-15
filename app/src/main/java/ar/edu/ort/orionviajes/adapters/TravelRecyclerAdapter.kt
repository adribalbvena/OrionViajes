package ar.edu.ort.orionviajes.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.data.TravelX
import ar.edu.ort.orionviajes.databinding.ItemTravelBinding
import ar.edu.ort.orionviajes.fragments.MyTravelsFragmentDirections

//Version fea vieja
//class TravelRecyclerAdapter: RecyclerView.Adapter<TravelRecyclerAdapter.TravelHolder>() {
//
//    var travelList = mutableListOf<TravelX>()
//    class TravelHolder(val binding: ItemTravelBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelHolder {
////        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
////        return TravelHolder(inflater)
//        val inflater = ContextCompat.getSystemService(parent.context, LayoutInflater::class.java) as LayoutInflater
//        val binding = ItemTravelBinding.inflate(inflater, parent, false)
//        return TravelHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: TravelHolder, position: Int) {
//        val item = travelList[position]
//        holder.binding.tvTravelTitle.text = item.title
//        holder.binding.tvTravelBudget.text = item.budget.toString()
//        holder.binding.tvTravelStartDate.text = item.startDate
//        holder.binding.tvTravelEndDate.text = item.endDate
//    }
//
//    override fun getItemCount(): Int {
//        return travelList.size
//    }
//
//}
//val onClick: (TravelX) -> Unit

class TravelRecyclerAdapter(): ListAdapter<TravelX, TravelRecyclerAdapter.TravelViewHolder>(
    TravelDiffutilCallback()
) {

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

        holder.binding.btnEditTravel.setOnClickListener{
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToEditTravelFragment(item)
            it.findNavController().navigate(action)
           // onClick(item)
        }

        holder.binding.btnArrowTravel.setOnClickListener{
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToExpensesFragment(item)
            it.findNavController().navigate(action)
        }
    }

}