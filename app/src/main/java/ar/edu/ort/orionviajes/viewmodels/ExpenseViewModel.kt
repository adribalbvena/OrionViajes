package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.api.ApiClient

import ar.edu.ort.orionviajes.data.ExpensesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpenseViewModel(private val context: Context) : ViewModel() {

    var expenses: MutableLiveData<ExpensesResponse?>

    init {
        expenses = MutableLiveData()
    }

    fun getExpenses(travel_id: String) {
        val apiService = ApiClient.getTravelsApi(context)
        val call = apiService.getExpenses(travel_id)
        call.enqueue(object : Callback<ExpensesResponse> {
            override fun onResponse(
                call: Call<ExpensesResponse>,
                response: Response<ExpensesResponse>
            ) {
                if (response.isSuccessful) {
                    expenses.postValue(response.body())
                } else {
                    expenses.postValue(null)
                }
            }

            override fun onFailure(call: Call<ExpensesResponse>, t: Throwable) {
                expenses.postValue(null)
            }

        })

    }

}