package ar.edu.ort.orionviajes.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.databinding.ItemExpenseBinding

class ExpenseRecyclerAdapter() : ListAdapter<Expense, ExpenseRecyclerAdapter.ExpenseViewHolder>(ExpenseDiffutilCallback()) {

   // private lateinit var binding: ItemExpenseBinding

    class ExpenseViewHolder(val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root)

    class ExpenseDiffutilCallback: DiffUtil.ItemCallback<Expense>(){
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem === newItem
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExpenseBinding.inflate(inflater, parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.tvExpenseTitle.text = item.title
        holder.binding.tvAmount.text = item.amount.toString()
        holder.binding.tvCurrency.text = item.currency
        holder.binding.categoryExpense.text = item.category
        holder.binding.payMethodExpense.text = item.paymentMethod
        holder.binding.tvExpenseDate.text = item.date

    }

}