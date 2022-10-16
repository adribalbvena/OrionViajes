package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.databinding.FragmentCreateExpenseBinding

class CreateExpenseFragment : Fragment() {
    private lateinit var binding: FragmentCreateExpenseBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateExpenseBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

}