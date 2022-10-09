package ar.edu.ort.orionviajes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.databinding.FragmentMyTravelsBinding
import com.google.android.material.snackbar.Snackbar


class MyTravelsFragment : Fragment() {
    companion object {
        fun newInstance() = MyTravelsFragment()
    }

    private var _binding: FragmentMyTravelsBinding? = null
    private val binding get() = _binding!!

    private lateinit var travelViewModel : TravelViewModel

    private val travelRecyclerAdapter by lazy {
        TravelRecyclerAdapter()
    }

    private lateinit var linearLayoutManager: LinearLayoutManager

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMyTravelsBinding.inflate(inflater,container,false)
        val view = binding.root
        //return inflater.inflate(R.layout.fragment_my_travels, container, false)
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)

        linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.travelsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = travelRecyclerAdapter
        }

        travelViewModel.getTravels()

        travelViewModel.travels.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Success -> {
                    travelRecyclerAdapter.submitList(it.data)
                }
                is Resource.Error -> {
                    Snackbar.make(binding.root, it.message.toString() ,Snackbar.LENGTH_LONG).show()
                }
            }
        })

    }
}