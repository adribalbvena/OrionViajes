package ar.edu.ort.orionviajes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.ort.orionviajes.R
import android.os.Handler
import android.util.Log
import ar.edu.ort.orionviajes.SessionManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val token = SessionManager(this).fetchAuthToken()

        if (token == null) {
            startLoginActivity()
            return
        }

        startMainActivity()
    }

    fun startLoginActivity() {
        this.startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        this.startActivity(intent)
        this.finish()
    }
}