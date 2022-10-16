package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient.getTravelsApi
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateExpenseViewModel(private val context: Context): ViewModel() {

    var addExpense: MutableLiveData<SingleExpenseResponse?> = MutableLiveData()

    fun addExpense(travel_id: String, expense: Expense) {
        val apiService = getTravelsApi(context)
        val call = apiService.addExpense(travel_id, expense)
        call.enqueue(object : Callback<SingleExpenseResponse>{
            override fun onResponse(
                call: Call<SingleExpenseResponse>,
                response: Response<SingleExpenseResponse>
            ) {
                if (response.isSuccessful){
                    addExpense.postValue(response.body())
                } else {
                    addExpense.postValue(null)
                }
            }

            override fun onFailure(call: Call<SingleExpenseResponse>, t: Throwable) {
                addExpense.postValue(null)
            }

        })

    }

}