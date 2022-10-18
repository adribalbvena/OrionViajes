package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.adapters.TravelRecyclerAdapter
import ar.edu.ort.orionviajes.viewmodels.TravelViewModel
import ar.edu.ort.orionviajes.data.Travel
import ar.edu.ort.orionviajes.databinding.FragmentMyTravelsBinding
import ar.edu.ort.orionviajes.factories.TravelViewModelFactory
import ar.edu.ort.orionviajes.listener.OnTravelClickedListener
import com.google.android.material.snackbar.Snackbar


class MyTravelsFragment : Fragment(), OnTravelClickedListener {
    companion object {
        fun newInstance() = MyTravelsFragment()
    }

    private lateinit var binding: FragmentMyTravelsBinding

    private lateinit var travelViewModel: TravelViewModel
    private lateinit var travelRecyclerAdapter: TravelRecyclerAdapter

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyTravelsBinding.inflate(inflater,container,false)
        val view = binding.root


        initTravelsRecyclerView()
        initTravelsViewModel()

        return view

    }


    private fun initTravelsRecyclerView() {
        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        //travelViewModel.getTravels()

        binding.travelsRecyclerView.setHasFixedSize(true)
        binding.travelsRecyclerView.layoutManager = linearLayoutManager
        travelRecyclerAdapter = TravelRecyclerAdapter(this)
        binding.travelsRecyclerView.adapter = travelRecyclerAdapter

        /*binding.travelsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            travelViewModel.getTravels()
            travelRecyclerAdapter = TravelRecyclerAdapter(this)
            adapter = travelRecyclerAdapter
        }*/
    }


    fun initTravelsViewModel() {
        activity?.let {
            travelViewModel =
                ViewModelProvider(this, TravelViewModelFactory(it)).get(TravelViewModel::class.java)
        }

        travelViewModel.travels.observe(viewLifecycleOwner, Observer {
            if (it == null) {
                Snackbar.make(binding.root, R.string.errorLoadingTravels ,Snackbar.LENGTH_LONG).show()
            } else {
                travelRecyclerAdapter.updateList(it)
            }
        })

        travelViewModel.getTravels()
    }


    override fun onStart() {
        super.onStart()
        binding.normalFAB.setOnClickListener() {
            val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToCreateTravelFragment()
            findNavController().navigate(action)
        }
    }

    override fun onTravelSelected(travel: Travel) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToExpensesFragment(travel)
        findNavController().navigate(action)
    }

    override fun onTravelEditClick(travel: Travel) {
        val action = MyTravelsFragmentDirections.actionMyTravelsFragmentToEditTravelFragment(travel)
        findNavController().navigate(action)
    }


}