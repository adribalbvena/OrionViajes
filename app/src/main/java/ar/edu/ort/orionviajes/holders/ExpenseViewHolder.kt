package ar.edu.ort.orionviajes.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.Expense
import com.google.android.material.chip.Chip
import org.w3c.dom.Text

class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView
    private val currency: TextView
    private val amount: TextView
    private val category: Chip
    private val paymentMethod: Chip
    private val date: TextView

    init {
        title = itemView.findViewById(R.id.tvExpenseTitle)
        currency = itemView.findViewById(R.id.tvCurrency)
        amount = itemView.findViewById(R.id.tvAmount)
        category = itemView.findViewById(R.id.categoryExpense)
        paymentMethod = itemView.findViewById(R.id.payMethodExpense)
        date = itemView.findViewById(R.id.tvExpenseDate)
    }

    fun bind(expense: Expense) {
        title.text = expense.title
        currency.text = expense.currency
        amount.text = expense.amount.toString()
        category.text = expense.category
        paymentMethod.text = expense.paymentMethod
        date.text = expense.date
    }


}