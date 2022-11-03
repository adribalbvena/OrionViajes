package ar.edu.ort.orionviajes.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.Constants
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.api.ApiClient
import ar.edu.ort.orionviajes.data.CreateExpenseDto
import ar.edu.ort.orionviajes.data.Expense
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import ar.edu.ort.orionviajes.databinding.FragmentEditExpenseBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditExpenseFragment : Fragment() {

    private lateinit var binding: FragmentEditExpenseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditExpenseBinding.inflate(inflater, container, false)
        val view = binding.root

        val expense = EditExpenseFragmentArgs.fromBundle(requireArguments()).expense
        val travel = EditExpenseFragmentArgs.fromBundle(requireArguments()).travel
        loadExpense(expense)

        binding.btnUpdateExpense.setOnClickListener{
            updateExpense(travel.id, expense.id)
        }

        binding.btnDeleteExpense.setOnClickListener{
            showAlertDialog(travel.id, expense.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
        categoryPicker()
        paymentMethodPicker()
    }

    fun showAlertDialog(travel_id: String, expense_id: String) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.deteleTavelLabel)
        builder.setMessage(R.string.areYouShureDeleteExpense)
        builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
            deleteExpense(travel_id, expense_id)
            dialog.cancel()
        })
        builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()
    }

    private fun deleteExpense(travel_id: String, expense_id: String){
        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.deleteExpense(travel_id, expense_id)
        call.enqueue(object : Callback<SingleExpenseResponse>{
            override fun onResponse(
                call: Call<SingleExpenseResponse>,
                response: Response<SingleExpenseResponse>
            ) {
                Snackbar.make(requireView(), response.body()!!.message.toString(), Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }

            override fun onFailure(call: Call<SingleExpenseResponse>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL ELIMINAR GASTO", t.toString())
            }

        })

    }

    private fun updateExpense(travel_id : String, expense_id : String){
        val title = binding.editTextTitleExpenseEdit.text.toString()
        val currency = binding.autoCompleteTxtViewEditCurrency.text.toString()
        val amount = binding.editTextAmountExpenseEdit.text.toString()
        val category =  binding.autoCompleteTxtViewEditCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewEditPaymentMethod.text.toString()
        val date = binding.dateExpenseEditTil.text.toString()

        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category, paymentMethod, date)

        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.updateExpense(travel_id, expense_id, expense)
        call.enqueue(object : Callback<SingleExpenseResponse>{
            override fun onResponse(
                call: Call<SingleExpenseResponse>,
                response: Response<SingleExpenseResponse>
            ) {
                Snackbar.make(requireView(), response.body()!!.message.toString(), Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }

            override fun onFailure(call: Call<SingleExpenseResponse>, t: Throwable) {
                Snackbar.make(requireView(), R.string.somethingWentWrong, Snackbar.LENGTH_LONG).show()
                Log.d("ERROR AL CARGAR GASTO", t.toString())
            }
        })
    }

    private fun loadExpense(expense: Expense){
        binding.editTextTitleExpenseEdit.setText(expense.title)
        binding.autoCompleteTxtViewEditCurrency.setText(expense.currency)
        binding.editTextAmountExpenseEdit.setText(expense.amount.toString())
        binding.autoCompleteTxtViewEditCategory.setText(expense.category)
        binding.autoCompleteTxtViewEditPaymentMethod.setText(expense.paymentMethod)
        binding.dateExpenseEditTil.text = expense.date
    }

    fun paymentMethodPicker() {
        val paymentMethod = Constants.PAYMENT_METHOD
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, paymentMethod)
        with(binding.autoCompleteTxtViewEditPaymentMethod){
            setAdapter(adapter)
        }
    }

    fun categoryPicker() {
        val category = Constants.CATEGORIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, category)
        with(binding.autoCompleteTxtViewEditCategory){
            setAdapter(adapter)
        }
    }

    fun currencyPicker() {
        val currency = Constants.CURRENCIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewEditCurrency){
            setAdapter(adapter)
        }
    }


    fun datePicker(view: View){
        binding = FragmentEditExpenseBinding.bind(view)

        binding.apply {
            dateExpenseEditTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    dateExpenseEditTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }
        }

    }
}