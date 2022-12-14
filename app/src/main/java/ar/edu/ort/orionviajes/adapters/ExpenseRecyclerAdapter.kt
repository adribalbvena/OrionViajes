package ar.edu.ort.orionviajes.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.holders.ExpenseViewHolder
import ar.edu.ort.orionviajes.listener.OnExpenseClickedListener

class ExpenseRecyclerAdapter(
    private val onExpenseClickedListener: OnExpenseClickedListener
): RecyclerView.Adapter<ExpenseViewHolder>() {

    var expenseList: List<Expense> = emptyList()

    override fun getItemCount() = expenseList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.bind(expense)

        holder.itemView.setOnClickListener {
            onExpenseClickedListener.onExpenseSelected(expense)
        }

    }

    fun updateList(expense: List<Expense>) {
        this.expenseList = expense
        notifyDataSetChanged();
    }


}