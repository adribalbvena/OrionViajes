package ar.edu.ort.orionviajes.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.viewmodels.ExpenseViewModel
import ar.edu.ort.orionviajes.adapters.ExpenseRecyclerAdapter
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.ExpensesResponse
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentExpensesBinding
import ar.edu.ort.orionviajes.factories.ExpenseViewModelFactory
import ar.edu.ort.orionviajes.factories.ExpensesListViewModelFactory
import ar.edu.ort.orionviajes.listener.OnExpenseClickedListener
import ar.edu.ort.orionviajes.viewmodels.ExpensesListViewModel
import com.google.android.material.snackbar.Snackbar


class ExpensesFragment : Fragment(), MenuProvider, OnExpenseClickedListener {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var expenseRecyclerAdapter: ExpenseRecyclerAdapter
    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var count: Float = 0.0f
    private lateinit var expensesList: ExpensesResponse


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding.root

        val travel = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId

        initExpensesRecyclerView()
        initExpenseViewModel(travel)

        val progressBar = binding.progressBarBudget
        progressBar.max = travel.budget.toInt()


        binding.btnAddExpense.setOnClickListener {
            binding.btnFormExpense.visibility = View.VISIBLE
            binding.btnScanExpense.visibility = View.VISIBLE
        }

        binding.btnFormExpense.setOnClickListener{
            val action = ExpensesFragmentDirections.actionExpensesFragmentToCreateExpenseFragment(travel)
            view.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
    }

    private fun getExpensesTotal(list : ExpensesResponse, budget: Float): Float {
        var count = 0.0F
        for (expense in list) {
            count += expense.amount
        }
        return (budget - count)
    }

    private fun initExpensesRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.expensesRecyclerView.setHasFixedSize(true)
        binding.expensesRecyclerView.layoutManager = linearLayoutManager
        expenseRecyclerAdapter = ExpenseRecyclerAdapter(this)
        binding.expensesRecyclerView.adapter = expenseRecyclerAdapter
    }

    @SuppressLint("SetTextI18n")
    fun initExpenseViewModel(travel: Travel) {
        activity?.let {
            expenseViewModel = ViewModelProvider(
                this,
                ExpenseViewModelFactory(it)
            ).get(ExpenseViewModel::class.java)
        }

        expenseViewModel.expenses.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Snackbar.make(binding.root, "Error al cargar los gastos", Snackbar.LENGTH_LONG)
                    .show()
            } else {
                expenseRecyclerAdapter.updateList(it)

                //Aca hace esta guarangada para el contador de presupuesto restante
                expensesList = it
                count = getExpensesTotal(expensesList, travel.budget)
                binding.progressBarBudget.setProgress(count.toInt())
                binding.remainingBudgetNumbers.setText("${count}/${travel.budget}")

            }
        })

        expenseViewModel.getExpenses(travel.id)

    }

    override fun onExpenseSelected(expense: Expense) {
        val travelId = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId
        val action = ExpensesFragmentDirections.actionExpensesFragmentToEditExpenseFragment(expense,travelId)
        findNavController().navigate(action)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.expenses_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.dashboard){
            val action = ExpensesFragmentDirections.actionExpensesFragmentToReportsFragment()
            findNavController().navigate(action)
            return true
        }
        return false
    }


}