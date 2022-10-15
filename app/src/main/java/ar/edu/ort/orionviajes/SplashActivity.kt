package ar.edu.ort.orionviajes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val preferences = this.getSharedPreferences("orionviajespreferences", MODE_PRIVATE);

        val token = preferences.getString("token", null);

        if(token == null)
        {
            //load login activity
            finish()
        }

        //download all travels
        
        //load main activity
        finish()
    }
}