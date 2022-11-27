package net.valet.view.transact

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import net.valet.adapter.AdapterListAnimation
import net.valet.api.ApiEndPoint
import net.valet.databinding.ActivityListTransactBinding
import net.valet.helper.SessionManager
import net.valet.helper.SessionManagerApps
import net.valet.model.Transact
import net.valet.utils.ItemAnimation
import net.valet.utils.Tools
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ListTransactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListTransactBinding
    private lateinit var session: SessionManager
    private lateinit var sessionApps: SessionManagerApps
    private lateinit var recyclerViewTransact: RecyclerView
    private lateinit var mAdapter: AdapterListAnimation
    val items = ArrayList<Transact>()
    private val animationType: Int = ItemAnimation.BOTTOM_UP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListTransactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)

        initComponent()
        initToolbar()
        getListTransact()
    }

    private fun initComponent() {

        recyclerViewTransact = binding.recyclerView
        recyclerViewTransact.layoutManager = LinearLayoutManager(this)
        recyclerViewTransact.setHasFixedSize(true)
    }


    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getListTransact() {

        binding.layoutProgress.progressOverlay.visibility = View.VISIBLE
        binding.layoutProgress.textLoading.text = "Loading data"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(sessionApps.apiServer+ApiEndPoint.listTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("search","")
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE
                    Log.d("main-activity", response!!.toString())

                    val user = response.getJSONArray("data")

                    items.clear()

                    for (i in 0 until user.length()) {

                        items.add(
                            Transact(
                                user.getJSONObject(i).getString("REGNO"),
                                user.getJSONObject(i).getString("PASSNO"),
                                user.getJSONObject(i).getString("FACTNO"),
                                user.getJSONObject(i).getString("REMARK"),
                                user.getJSONObject(i).getString("LUPDDTTIME")
                            )
                        )

                    }

                    mAdapter = AdapterListAnimation(applicationContext, items, animationType, false)
                    recyclerViewTransact.adapter = mAdapter

                    mAdapter.setOnItemClickListener { _, obj, _ ->
                        //Toast.makeText(applicationContext,obj.username, Toast.LENGTH_LONG).show()
                        session.stringEditData = obj.passno
                        session.isCreate = false
                        startActivity(Intent(applicationContext, NewTransactActivity::class.java))
                    }

                }

                override fun onError(anError: ANError?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE
                    Log.d("main-activity", anError!!.message.toString())

                    val errorBody = JSONObject(anError.errorBody)

                    val error = errorBody.getString("message")

                    Tools.showError(this@ListTransactActivity,error)

                }

            })
    }
}