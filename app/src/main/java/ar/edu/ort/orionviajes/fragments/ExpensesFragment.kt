package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.ExpenseViewModel
import ar.edu.ort.orionviajes.adapters.ExpenseRecyclerAdapter
import ar.edu.ort.orionviajes.databinding.FragmentExpensesBinding
import ar.edu.ort.orionviajes.factories.ExpenseViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlin.math.exp


class ExpensesFragment : Fragment() {
    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!

    private lateinit var expenseRecyclerAdapter: ExpenseRecyclerAdapter

    private lateinit var expenseViewModel: ExpenseViewModel

    private lateinit var linearLayoutManager: LinearLayoutManager

    //private val travel = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding.root

        val travel = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId

        initExpensesRecyclerView()
        initExpenseViewModel(travel.id)

        binding.btnAddExpense.setOnClickListener {
            binding.btnFormExpense.visibility = View.VISIBLE
            binding.btnScanExpense.visibility = View.VISIBLE
        }

        return view
    }

    private fun initExpensesRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.expensesRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            expenseRecyclerAdapter = ExpenseRecyclerAdapter()
            adapter = expenseRecyclerAdapter
        }
    }

    fun initExpenseViewModel(travel_id: String) {
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
                expenseRecyclerAdapter.submitList(it)
            }
        })

        expenseViewModel.getExpenses(travel_id)
    }

}