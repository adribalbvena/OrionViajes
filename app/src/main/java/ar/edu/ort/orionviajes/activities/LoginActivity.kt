package ar.edu.ort.orionviajes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import ar.edu.ort.orionviajes.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportFragmentManager.setFragmentResultListener(
            "login", this
        ) { _, _ -> startMainActivity() }
    }

    private fun startMainActivity() {
        this.startActivity(Intent(this, MainActivity::class.java))
        this.finish()
    }
}