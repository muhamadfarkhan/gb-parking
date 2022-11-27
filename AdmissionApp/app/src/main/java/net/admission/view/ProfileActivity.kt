package net.admission.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import net.admission.databinding.ActivityProfileBinding
import net.admission.helper.SessionManager
import android.view.Menu
import net.admission.utils.Tools
import android.widget.Toast
import android.view.MenuItem
import net.admission.R
import net.admission.helper.SessionManagerApps

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var session: SessionManager
    private lateinit var sessionApps: SessionManagerApps

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)

        initButton()
        initToolbar()
        initComponent()
    }

    private fun initComponent() {
        binding.userName.text = session.username
        binding.userDetail.text = session.fullname
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initButton() {
        binding.btnLogout.setOnClickListener {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Are you sure ?")
            builder.setPositiveButton("Logout"
            ) { _, _ ->
                session.logout()
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
            }
            builder.setNegativeButton("cancel", null)
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_close, menu)
        Tools.changeMenuIconColor(menu, resources.getColor(R.color.yellow_700))
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId === android.R.id.home || item.itemId === R.id.action_close) {
            finish()
        } else {
            Toast.makeText(applicationContext, item.title, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}