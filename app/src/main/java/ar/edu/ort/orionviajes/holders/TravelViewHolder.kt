package ar.edu.ort.orionviajes.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.TravelX

class TravelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView
    private val budget: TextView
    private val startDate: TextView
    private val endDate: TextView
    private val btnEditTravel: TextView

    init {
        title = itemView.findViewById(R.id.tvTravelTitle)
        budget = itemView.findViewById(R.id.tvTravelBudget)
        startDate = itemView.findViewById(R.id.tvTravelStartDate)
        endDate = itemView.findViewById(R.id.tvTravelEndDate)
        btnEditTravel = itemView.findViewById(R.id.btnEditTravel)
    }

    fun bind(travel: TravelX) {
        title.text = travel.title
        budget.text = travel.budget.toString()
        startDate.text = travel.startDate
        endDate.text = travel.endDate
    }

    fun getBtnEditTravel(): TextView{
        return btnEditTravel
    }
}