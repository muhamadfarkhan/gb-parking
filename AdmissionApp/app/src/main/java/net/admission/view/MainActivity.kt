package net.admission.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.admission.databinding.ActivityMainBinding
import net.admission.helper.SessionManager
import net.admission.view.transact.NewTransactActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        initButton()
        initComponent()
    }

    private fun initComponent() {
        binding.userName.text = session.username
        binding.userDetail.text = session.fullname
    }

    private fun initButton() {
        binding.btnNewTransact.setOnClickListener {
            val intent = Intent(this@MainActivity, NewTransactActivity::class.java)
            startActivity(intent)
        }
        binding.btnGoProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.userName.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.userDetail.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }
}