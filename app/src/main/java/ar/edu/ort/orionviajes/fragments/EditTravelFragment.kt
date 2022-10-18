package ar.edu.ort.orionviajes.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.data.CreateTravelDto
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentEditTravelBinding
import ar.edu.ort.orionviajes.factories.EditDeleteTravelViewModelFactory
import ar.edu.ort.orionviajes.viewmodels.EditDeleteTravelViewModel
import com.google.android.material.snackbar.Snackbar


class EditTravelFragment : Fragment() {

    private lateinit var binding: FragmentEditTravelBinding

    private lateinit var editDeleteTravelViewModel: EditDeleteTravelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTravelBinding.inflate(inflater, container, false)
        val view = binding.root

        initTravelViewModel()
        addUpdateObservable()
        //addDeleteObservable()

        val travel = EditTravelFragmentArgs.fromBundle(requireArguments()).travel
        loadTravel(travel)

        binding.btnUpdateTravel.setOnClickListener {
            updateTravel(travel.id)
        }

        binding.btnDeleteTravel.setOnClickListener {
            showAlertDialog(travel.id)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        datePicker(view)
    }

    fun showAlertDialog(travel_id: String) {
        var builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.deteleTavelLabel)
        builder.setMessage(R.string.areYouShureDeleteTravel)
        builder.setPositiveButton(R.string.yes, DialogInterface.OnClickListener { dialog, id ->
            editDeleteTravelViewModel.deleteTravel(travel_id)
            Snackbar.make(binding.root, R.string.successDeletedTravel, Snackbar.LENGTH_LONG).show()
            findNavController().navigateUp()
            dialog.cancel()
        })
        builder.setNegativeButton(R.string.no, DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
        })
        var alert = builder.create()
        alert.show()
    }


    private fun addUpdateObservable() {
        editDeleteTravelViewModel.updateTravel.observe(viewLifecycleOwner, Observer {
            Snackbar.make(binding.root, R.string.successUpdateTravel, Snackbar.LENGTH_LONG).show()
            //Utilizar siempre el findNavController ya que estas con el NavGraph
            findNavController().navigateUp()

            //Esta cambiando el NavController y luego se va a romper.
            //activity?.supportFragmentManager?.popBackStack()

        })
    }

    fun updateTravel(travel_id: String) {
        val title = binding.editTextTitleTravelEdit.text.toString()
        val budget = binding.editTextBudgetTravelEdit.text.toString()
        val startDate = binding.startDateEditTil.text.toString()
        val endDate = binding.endDateEditTil.text.toString()

        val travel = CreateTravelDto(title, budget.toFloat(), startDate, endDate)

        editDeleteTravelViewModel.updateTravel(travel_id, travel)
    }

    private fun loadTravel(travel: Travel) {
        binding.editTextTitleTravelEdit.setText(travel.title)
        binding.editTextBudgetTravelEdit.setText(travel.budget.toString())
        binding.startDateEditTil.setText(travel.startDate)
        binding.endDateEditTil.setText(travel.endDate)
    }

    private fun initTravelViewModel() {
        activity?.let {
            editDeleteTravelViewModel =
                ViewModelProvider(this, EditDeleteTravelViewModelFactory(it)).get(
                    EditDeleteTravelViewModel::class.java
                )
        }
    }

    fun datePicker(view: View) {
        binding = FragmentEditTravelBinding.bind(view)

        binding.apply {
            startDateEditTil.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date = bundle.getString("SELECTED_DATE")
                        startDateEditTil.text = date
                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

            endDateEditTil.setOnClickListener {
                val datePickerFragment = DatePickerFragment()
                val supportFragmentManager = requireActivity().supportFragmentManager

                supportFragmentManager.setFragmentResultListener(
                    "REQUEST_KEY",
                    viewLifecycleOwner
                ) { resultKey, bundle ->
                    if (resultKey == "REQUEST_KEY") {
                        val date2 = bundle.getString("SELECTED_DATE")
                        endDateEditTil.text = date2

                    }
                }

                datePickerFragment.show(supportFragmentManager, "DatePickerFragment")

            }

        }

    }

}