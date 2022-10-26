package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.GetTravelsResponse
import ar.edu.ort.orionviajes.data.Travel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//
//class TravelViewModel(private val context: Context) : ViewModel() {
//    private var _travels = MutableLiveData<Resource<List<Travel>>>()
//    val travels: LiveData<Resource<List<Travel>>> = _travels
//
//    val travelRepository by lazy {
//        TravelRepository()
//    }
//
//    fun getTravels() {
//        viewModelScope.launch {
//            _travels.postValue(travelRepository.getTravels())
//        }
//    }
//}


class TravelViewModel(private val context: Context) : ViewModel() {
//    private var _travels = MutableLiveData<List<TravelX>?>()
//    val travels: LiveData<List<TravelX>?> = _travels
//
//    private var _addTravel = MutableLiveData<List<TravelX>?>()
//    val addTravel: LiveData<List<TravelX>?> = _addTravel

    var travels: MutableLiveData<GetTravelsResponse?>

    init {
        travels = MutableLiveData()
    }

    fun getTravels() {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.getTravels()
        call.enqueue(object : Callback<GetTravelsResponse> {
            override fun onResponse(
                call: Call<GetTravelsResponse>,
                response: Response<GetTravelsResponse>
            ) {
                if (response.isSuccessful) {
                    travels.postValue(response.body())
                } else {
                    //aca voy a hacer en todos if response.code == 401
                    // if this is login fragment credenciales invalidas y sino, logout
                    //if response.errorBody, algo salio mal
                    travels.postValue(null)
                }
            }

            override fun onFailure(call: Call<GetTravelsResponse>, t: Throwable) {
                travels.postValue(null)
            }

        })
    }
}