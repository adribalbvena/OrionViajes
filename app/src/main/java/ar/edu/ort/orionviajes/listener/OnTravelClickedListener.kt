package ar.edu.ort.orionviajes.listener

import ar.edu.ort.orionviajes.data.TravelX

interface OnTravelClickedListener {
    fun onTravelSelected(travel: TravelX)
    fun onTravelEditClick(travel: TravelX)
}