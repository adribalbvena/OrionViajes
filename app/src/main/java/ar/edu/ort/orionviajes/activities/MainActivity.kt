package ar.edu.ort.orionviajes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ar.edu.ort.orionviajes.R
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.navHost)
        binding.navView.setupWithNavController(navController)

        val navHeader = binding.navView.getHeaderView(0)
        val txtUser = navHeader.findViewById<TextView>(R.id.userTv)
        txtUser.text = SessionManager(this).getUser()

        NavigationUI.setupActionBarWithNavController(this,navController, binding.drawerLayout)
        
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        return NavigationUI.navigateUp(navController, binding.drawerLayout)
    }

}