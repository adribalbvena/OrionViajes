package ar.edu.ort.orionviajes.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.holders.TravelViewHolder
import ar.edu.ort.orionviajes.listener.OnTravelClickedListener

class TravelRecyclerAdapter(
    private val onTravelClickedListener: OnTravelClickedListener
): RecyclerView.Adapter<TravelViewHolder>() {

    var travelList: List<Travel> = emptyList()

    override fun getItemCount() = travelList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TravelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_travel, parent, false)
        return TravelViewHolder(view)
    }

    override fun onBindViewHolder(holder: TravelViewHolder, position: Int) {
        val travel = travelList[position]
        holder.bind(travel)

        //Editar
        holder.getBtnEditTravel().setOnClickListener{
            onTravelClickedListener.onTravelEditClick(travel)
        }

        //Ver Gastos
        holder.itemView.setOnClickListener {
            onTravelClickedListener.onTravelSelected(travel)
        }
    }

    fun updateList(travels: List<Travel>){
        this.travelList = travels
        notifyDataSetChanged();
    }
}