package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTravelViewModel(private val context: Context): ViewModel() {

    var addTravel: MutableLiveData<GetTravelsResponse?>

    init{
        addTravel = MutableLiveData()
    }

    fun addTravel(travel : TravelX){
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.addTravel(travel)
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if(response.isSuccessful){
                    addTravel.postValue(response.body())
                } else {
                    addTravel.postValue(null)
                }
            }
            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                addTravel.postValue(null)
            }

        })
    }

}