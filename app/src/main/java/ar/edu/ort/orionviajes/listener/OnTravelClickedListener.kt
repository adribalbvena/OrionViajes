package ar.edu.ort.orionviajes.listener

import ar.edu.ort.orionviajes.data.Travel

interface OnTravelClickedListener {
    fun onTravelSelected(travel: Travel)
    fun onTravelEditClick(travel: Travel)
}