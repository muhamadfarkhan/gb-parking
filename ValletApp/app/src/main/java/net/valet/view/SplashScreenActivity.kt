package net.valet.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import net.valet.R
import net.valet.helper.SessionManager
import net.valet.helper.SessionManagerApps

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var session: SessionManager
    private lateinit var sessionApps: SessionManagerApps
    var apiServer = "http://api-submission.farkhan.net"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)
        apiServer = sessionApps.apiServer

        Handler().postDelayed({
            if(session.isLogin){
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
            }else{
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            }

            finish()
        }, 1500)
    }
}