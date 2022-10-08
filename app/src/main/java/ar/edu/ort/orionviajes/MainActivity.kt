package ar.edu.ort.orionviajes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.ort.orionviajes.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null

    private val binding get() = _binding!!

    private lateinit var travelViewModel : TravelViewModel

    private val travelRecyclerAdapter by lazy {
        TravelRecyclerAdapter()
    }

    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelViewModel = ViewModelProvider(this).get(TravelViewModel::class.java)


        linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.travelsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = travelRecyclerAdapter
        }

        travelViewModel.getTravels()



        travelViewModel.travels.observe(this, Observer {
            //travelRecyclerAdapter.submitList(it)
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