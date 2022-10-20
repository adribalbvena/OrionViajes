package ar.edu.ort.orionviajes.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ar.edu.ort.orionviajes.fragments.CategoryFragment
import ar.edu.ort.orionviajes.fragments.PaymentMethodFragment

//guarda los estados en el activity
class ViewPagerAdapter(fragmentActivity: FragmentActivity) :FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return TAB_COUNT

    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CategoryFragment()
            1 -> PaymentMethodFragment()
//            2 -> dateFragment()
            else -> CategoryFragment()
        }
    }

    companion object{
        private const val TAB_COUNT = 2
    }
}