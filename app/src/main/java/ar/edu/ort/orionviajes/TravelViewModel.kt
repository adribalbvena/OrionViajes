package ar.edu.ort.orionviajes

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient.apiService
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.SingleTravelResponse
import ar.edu.ort.orionviajes.data.TravelX
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TravelViewModel: ViewModel() {
//    private var _travels = MutableLiveData<List<TravelX>?>()
//    val travels: LiveData<List<TravelX>?> = _travels
//
//    private var _addTravel = MutableLiveData<List<TravelX>?>()
//    val addTravel: LiveData<List<TravelX>?> = _addTravel

    lateinit var travels: MutableLiveData<GetTravelsResponse?>
    lateinit var singleTravel: MutableLiveData<SingleTravelResponse?>
    lateinit var addTravel: MutableLiveData<GetTravelsResponse?>
    lateinit var loadTravelData: MutableLiveData<SingleTravelResponse?>
    lateinit var deleteTravel: MutableLiveData<GetTravelsResponse?>

    init {
        travels = MutableLiveData()
        singleTravel = MutableLiveData()
        addTravel = MutableLiveData()
        loadTravelData = MutableLiveData()
        deleteTravel = MutableLiveData()
    }



    fun getTravels(){
        val apiService = apiService
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if(response.isSuccessful){
                    travels.postValue(response.body())
                } else {
                    travels.postValue(null)
                }
            }
            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                travels.postValue(null)
            }

        })
    }


    fun addTravel(travel : TravelX){
        val apiService = apiService
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