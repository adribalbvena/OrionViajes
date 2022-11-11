package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
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
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentCreateExpenseBinding
import ar.edu.ort.orionviajes.hideKeyboard
import com.google.android.material.snackbar.Snackbar

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CreateExpenseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExpenseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateExpenseBinding.inflate(inflater, container, false)
        val view = binding.root

        val travel = CreateExpenseFragmentArgs.fromBundle(requireArguments()).travelId

        loadData(travel.currency)

        binding.btnAddExpense.setOnClickListener{
            it.hideKeyboard()
            if(!validateInputs()) {
                return@setOnClickListener
            }
            addExpense(travel)
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        categoryPicker()
        paymetMethodPicker()
    }

    private fun validateInputs() : Boolean{
        //validar los campos vacios
        if(TextUtils.isEmpty(binding.editTextTitleExpense.text) || TextUtils.isEmpty(binding.autoCompleteTxtViewCurrency.text)
            || TextUtils.isEmpty(binding.editTextAmountExpense.text) || TextUtils.isEmpty(binding.autoCompleteTxtViewCategory.text)
            || TextUtils.isEmpty(binding.autoCompleteTxtViewPaymentMethod.text) || TextUtils.isEmpty(binding.dateExpenseTil.text)) {
            Snackbar.make(requireView(), R.string.emptyFields, Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun addExpense(travel: Travel) {
        val title = binding.editTextTitleExpense.text.toString()
        val currency = travel.currency
        val amount = binding.editTextAmountExpense.text.toString()
        val category = binding.autoCompleteTxtViewCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewPaymentMethod.text.toString()
        val date = binding.dateExpenseTil.text.toString()

        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category,paymentMethod,date)

        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.addExpense(travel.id, expense)
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

    fun loadData(currency : String){
        val c = Calendar.getInstance()
        val date = SimpleDateFormat("dd-MM-yyyy", Locale("es", "ES")).format(c.time)
        binding.dateExpenseTil.text = date
        binding.autoCompleteTxtViewCurrency.setText(currency)
    }


    fun paymetMethodPicker() {
        val paymentMethod = Constants.PAYMENT_METHOD
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, paymentMethod)
        with(binding.autoCompleteTxtViewPaymentMethod){
            setAdapter(adapter)
        }
    }

    fun categoryPicker() {
        val category = Constants.CATEGORIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, category)
        with(binding.autoCompleteTxtViewCategory){
            setAdapter(adapter)
        }
    }

    fun datePicker(view: View){
        binding = FragmentCreateExpenseBinding.bind(view)

        binding.apply {
            dateExpenseTil.setOnClickListener{
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner) {
                        resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")
                    dateExpenseTil.text = date
                }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }
        }

    }



}