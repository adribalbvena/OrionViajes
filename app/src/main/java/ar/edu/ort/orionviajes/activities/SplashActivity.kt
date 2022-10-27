package ar.edu.ort.orionviajes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ar.edu.ort.orionviajes.R
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import ar.edu.ort.orionviajes.SessionManager
import ar.edu.ort.orionviajes.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    //private lateinit var binding: ActivitySplashBinding

//    companion object {
//        private const val SPLASH_TIME_OUT: Long = 3000 // 3 seconds
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        Handler().postDelayed(
//            {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            }, SPLASH_TIME_OUT)

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
