package net.valet.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import net.valet.adapter.AdapterListAnimation
import net.valet.api.ApiEndPoint
import net.valet.databinding.ActivityMainBinding
import net.valet.helper.SessionManager
import net.valet.helper.SessionManagerApps
import net.valet.model.Transact
import net.valet.utils.ItemAnimation
import net.valet.utils.Tools
import net.valet.view.transact.ListTransactActivity
import net.valet.view.transact.NewTransactActivity
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var session: SessionManager
    private lateinit var sessionApps: SessionManagerApps
    private lateinit var recyclerViewTransact: RecyclerView
    private lateinit var mAdapter: AdapterListAnimation
    val items = ArrayList<Transact>()
    private val animationType: Int = ItemAnimation.BOTTOM_UP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)

        initButton()
        initComponent()
        getListTransact("")
    }

    private fun initComponent() {
        binding.userName.text = session.username
        binding.userDetail.text = session.fullname

        recyclerViewTransact = binding.recyclerView
        recyclerViewTransact.layoutManager = LinearLayoutManager(this)
        recyclerViewTransact.setHasFixedSize(true)

        binding.etSearch.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun afterTextChanged(s: Editable) {
                    getListTransact(s.toString())
                }
            }
        )


        binding.etSearch.clearFocus()
    }

    private fun initButton() {
        binding.btnSetting.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }
        binding.btnRefresh.setOnClickListener {
            getListTransact("")
        }
        binding.loadMore.setOnClickListener {
            val intent = Intent(this@MainActivity, ListTransactActivity::class.java)
            startActivity(intent)
        }
        binding.btnNewTransact.setOnClickListener {
            session.isCreate = true
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


    private fun getListTransact(search: String) {

        binding.progressIndeterminate.visibility = View.VISIBLE

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(sessionApps.apiServer+ApiEndPoint.listTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("search",search)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.progressIndeterminate.visibility = View.GONE
                    Log.d("main-activity", response!!.toString())

                    val user = response.getJSONArray("data")

                    items.clear()

                    for (i in 0 until user.length()) {
                        if(i <= 5){
                            items.add(
                                Transact(user.getJSONObject(i).getString("PASSNO"),
                                    user.getJSONObject(i).getString("FACTNO"),
                                    user.getJSONObject(i).getString("REMARK"),
                                    user.getJSONObject(i).getString("LUPDDTTIME")
                                )
                            )
                        }
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

                    binding.progressIndeterminate.visibility = View.GONE
                    Log.d("main-activity", anError!!.message.toString())

                    val errorBody = JSONObject(anError.errorBody)

                    val error = errorBody.getString("message")

                    Tools.showError(this@MainActivity,error)

                }

            })
    }

    override fun onResume() {
        super.onResume()
        getListTransact("")
    }
}