package ar.edu.ort.orionviajes.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.activities.LoginActivity
import ar.edu.ort.orionviajes.databinding.FragmentLoginBinding

class LogoutFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        onLogOut()

        return binding.root
    }

    fun onLogOut(){
        val myActivity = this.requireActivity()
        SessionManager(myActivity).deleteAuthToken()
        SessionManager(myActivity).deleteUser()
        myActivity.startActivity(Intent(myActivity, LoginActivity::class.java))
        myActivity.finish()
    }

}