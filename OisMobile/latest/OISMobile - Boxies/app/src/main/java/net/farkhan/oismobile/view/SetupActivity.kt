package net.farkhan.oismobile.view

import android.os.Bundle
import android.app.Activity
import net.farkhan.oismobile.R

import kotlinx.android.synthetic.main.activity_setup.*
import net.farkhan.oismobile.utils.PrefHelper

class SetupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val prefHelper = PrefHelper(this@SetupActivity)

        val api = prefHelper.apiServer

        api_server.setText(api)

        save_button.setOnClickListener {
            prefHelper.apiServer = api_server.text.toString()
        }
    }

}
