package ar.edu.ort.orionviajes.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.adapters.ViewPagerAdapter
import ar.edu.ort.orionviajes.databinding.FragmentReportsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ReportsFragment : Fragment() {

    private lateinit var binding: FragmentReportsBinding

    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReportsBinding.inflate(inflater, container, false)
        val view = binding.root

        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        return view
    }

    override fun onStart() {
        super.onStart()

//        val args = this.arguments
//        val travelId = args?.getString("data")
//        Snackbar.make(binding.root, travelId.toString(), Snackbar.LENGTH_LONG).show()

        viewPager.setAdapter(ViewPagerAdapter(requireActivity()))

        TabLayoutMediator(tabLayout, viewPager, TabLayoutMediator.TabConfigurationStrategy{ tab, position ->
            when(position) {
                0 -> {
                    tab.text = "Categoria"
                }
                1 -> {
                    tab.text = "Metodo De Pago"
                }
                else -> tab.text = "undefined"
            }
        }).attach()
    }
}