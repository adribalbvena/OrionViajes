package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditDeleteTravelViewModel(private val context: Context) : ViewModel() {

    var updateTravel: MutableLiveData<SingleTravelResponse?>
    var deleteTravel: MutableLiveData<SingleTravelResponse?>

    init {
        updateTravel = MutableLiveData()
        deleteTravel = MutableLiveData()
    }

    fun updateTravel(travel_id: String, travel: TravelX) {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.updateTravel(travel_id, travel)
        call.enqueue(object : Callback<SingleTravelResponse> {
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if(response.isSuccessful){
                    updateTravel.postValue(response.body())
                } else {
                    updateTravel.postValue(null)

                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                updateTravel.postValue(null)
            }

        })
    }

    fun deleteTravel(travel_id: String) {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.deleteTravel(travel_id)
        call.enqueue(object : Callback<SingleTravelResponse> {
            override fun onResponse(
                call: Call<SingleTravelResponse>,
                response: Response<SingleTravelResponse>
            ) {
                if(response.isSuccessful) {
                    deleteTravel.postValue(response.body())
                } else {
                    deleteTravel.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleTravelResponse>, t: Throwable) {
                deleteTravel.postValue(null)
            }

        })
    }

}