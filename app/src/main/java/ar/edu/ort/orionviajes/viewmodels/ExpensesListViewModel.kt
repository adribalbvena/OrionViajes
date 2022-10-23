package ar.edu.ort.orionviajes.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.ExpensesResponse

class ExpensesListViewModel(private val context: Context) : ViewModel(){

     val mutableExpensesList = MutableLiveData<ArrayList<Expense>>()
//    val expensesList: LiveData<ExpensesResponse> get() = mutableExpensesList
//
//    fun expensesList(expenses : ExpensesResponse) {
//        mutableExpensesList.value = expenses
//    }
}