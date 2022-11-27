package net.valet.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.valet.databinding.ActivitySettingBinding
import net.valet.helper.SessionManager
import net.valet.helper.SessionManagerApps
import net.valet.utils.Global

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private lateinit var session: SessionManager
    private lateinit var sessionApps: SessionManagerApps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)
        initComponent()
        initButton()
    }

    private fun initButton() {
        binding.btnSubmitTransact.setOnClickListener {
            Global.apiServer = binding.etApiServer.text.toString()
            sessionApps.apiServer = binding.etApiServer.text.toString()
            val intent = Intent(this@SettingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun initComponent() {
        binding.etApiServer.setText(sessionApps.apiServer)
    }



}