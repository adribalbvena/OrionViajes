package ar.edu.ort.orionviajes.fragments

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
import ar.edu.ort.orionviajes.data.SingleExpenseResponse
import ar.edu.ort.orionviajes.databinding.FragmentCreateExpenseBinding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
        binding.btnAddExpense.setOnClickListener{
            addExpense(travel.id)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        datePicker(view)
        currencyPicker()
        categoryPicker()
        paymetMethodPicker()
    }

    private fun addExpense(travel_id: String) {
        val title = binding.editTextTitleExpense.text.toString()
        val currency = binding.autoCompleteTxtViewCurrency.text.toString()
        val amount = binding.editTextAmountExpense.text.toString()
        val category = binding.autoCompleteTxtViewCategory.text.toString()
        val paymentMethod = binding.autoCompleteTxtViewPaymentMethod.text.toString()
        val date = binding.dateExpenseTil.text.toString()

        val expense = CreateExpenseDto(title, currency, amount.toFloat(), category,paymentMethod,date)

        val apiService = ApiClient.getTravelsApi(requireContext())
        val call = apiService.addExpense(travel_id, expense)
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
        // (textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        with(binding.autoCompleteTxtViewCategory){
            setAdapter(adapter)
        }
    }

    fun currencyPicker() {
        val currency = Constants.CURRENCIES
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, currency)
        with(binding.autoCompleteTxtViewCurrency){
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