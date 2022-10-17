package ar.edu.ort.orionviajes.listener

import ar.edu.ort.orionviajes.data.Expense

interface OnExpenseClickedListener {
    fun onExpenseSelected(expense : Expense)
}