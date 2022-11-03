package ar.edu.ort.orionviajes.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.activities.LoginActivity
import ar.edu.ort.orionviajes.adapters.ExpenseRecyclerAdapter
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.ExpensesResponse
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentExpensesBinding
import ar.edu.ort.orionviajes.listener.OnExpenseClickedListener
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExpensesFragment : Fragment(), MenuProvider, OnExpenseClickedListener {
    private lateinit var binding: FragmentExpensesBinding
    private lateinit var expenseRecyclerAdapter: ExpenseRecyclerAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var count: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        val view = binding.root

        val travel = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId

        initExpensesRecyclerView()
        getExpenses(travel)

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

        binding.btnScanExpense.setOnClickListener{
            val action = ExpensesFragmentDirections.actionExpensesFragmentToScanFragment(travel)
            view.findNavController().navigate(action)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.addMenuProvider(this, viewLifecycleOwner)
    }

    private fun getExpensesTotal(list : ArrayList<Expense>, budget: Float): Float {
        var count = 0.0F
        for (expense in list) {
            count += expense.amount
        }
        val total = (budget - count)

        if(total < 0.0F){
            binding.remainingBudgetNumbers.setTextColor(getResources().getColor(R.color.orange_expense))
        }

        return total
    }

    private fun setBudgetBar(list : ArrayList<Expense>, travel : Travel){
        count = getExpensesTotal(list, travel.budget)
        binding.progressBarBudget.setProgress(count.toInt())
        binding.remainingBudgetNumbers.setText("${count}/${travel.budget}")

    }

    private fun initExpensesRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.expensesRecyclerView.setHasFixedSize(true)
        binding.expensesRecyclerView.layoutManager = linearLayoutManager
        expenseRecyclerAdapter = ExpenseRecyclerAdapter(this)
        binding.expensesRecyclerView.adapter = expenseRecyclerAdapter
    }


    private fun getExpenses(travel : Travel){
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.getExpenses(travel.id)
        call.enqueue(object : Callback<ExpensesResponse>{
            override fun onResponse(
                call: Call<ExpensesResponse>,
                response: Response<ExpensesResponse>
            ) {
                if (response.isSuccessful){
                    expenseRecyclerAdapter.updateList(response.body()!!)
                    setBudgetBar(response.body()!!, travel)

                } else {
                    if(response.code() == 401){
                        //logout
                        Snackbar.make(requireView(), "Tu sesión ha expirado! Vuelve a ingresar", Snackbar.LENGTH_LONG).show()
                        activity?.let {
                            SessionManager(it).deleteAuthToken()
                            activity?.finish()
                        }
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                    }

                }
            }

            override fun onFailure(call: Call<ExpensesResponse>, t: Throwable) {
                Snackbar.make(requireView(), "Ups! Algo salió mal", Snackbar.LENGTH_LONG).show()
            }

        })
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
        val travel = ExpensesFragmentArgs.fromBundle(requireArguments()).travelId
        if(menuItem.itemId == R.id.dashboard){
            val action = ExpensesFragmentDirections.actionExpensesFragmentToDashboardFragment(travel)
            findNavController().navigate(action)
            return true
        }
        return false
    }
}