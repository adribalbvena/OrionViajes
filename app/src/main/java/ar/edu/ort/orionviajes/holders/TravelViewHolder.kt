package ar.edu.ort.orionviajes.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.Travel

class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView
    private val startDate: TextView
    private val endDate: TextView
    private val btnEditTravel: TextView

    init {
        title = itemView.findViewById(R.id.tvTravelTitle)
        startDate = itemView.findViewById(R.id.tvTravelStartDate)
        endDate = itemView.findViewById(R.id.tvTravelEndDate)
        btnEditTravel = itemView.findViewById(R.id.btnEditTravel)
    }

    fun bind(travel: Travel) {
        title.text = travel.title
        startDate.text = travel.startDate
        endDate.text = travel.endDate
    }

    fun getBtnEditTravel(): TextView{
        return btnEditTravel
    }
}