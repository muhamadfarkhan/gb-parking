package net.farkhan.oismobile.view

import android.os.Bundle
import android.app.Activity

import kotlinx.android.synthetic.main.activity_print_out.*
import net.farkhan.oismobile.R

class PrintOutActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_out)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val intent = intent

        tvNotran.text = intent.getStringExtra("NOTRAN")
    }

}
