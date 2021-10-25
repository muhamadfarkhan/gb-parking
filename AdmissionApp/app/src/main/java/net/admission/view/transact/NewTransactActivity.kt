package net.admission.view.transact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import net.admission.api.ApiEndPoint
import net.admission.databinding.ActivityNewTransactBinding
import net.admission.helper.SessionManager
import net.admission.utils.Tools
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class NewTransactActivity : AppCompatActivity() {
    private lateinit var custIdVal: String
    private lateinit var prodIdVal: String
    private lateinit var noPlat: String
    private lateinit var name: String
    private lateinit var note: String
    private lateinit var productType: String
    private lateinit var binding: ActivityNewTransactBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTransactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        initComponent()
        initButton()
        initToolbar()
    }

    private fun initComponent() {
        populateCustomers()
        populateProds()
    }

    private fun initButton() {
        binding.btnSubmitTransact.setOnClickListener {
            submitTransact()
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun submitTransact() {
        noPlat = binding.etPlatNo.text.toString()
        name = binding.etUserName.text.toString()
        note = binding.etNote.text.toString()

        when {
            noPlat.isBlank() -> {
                binding.etPlatNo.error = "Please fill the blank"
            }
            name.isBlank() -> {
                binding.etUserName.error = "Please fill the blank"
            }
            note.isBlank() -> {
                binding.etNote.error = "Please fill the blank"
            }
            else -> {
                goSubmit()
            }
        }
    }

    private fun goSubmit() {
        binding.layoutProgress.progressOverlay.visibility = VISIBLE
        binding.layoutProgress.textLoading.text = "Processing data"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(ApiEndPoint.storeTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("factno", noPlat)
            .addBodyParameter("passno", noPlat)
            .addBodyParameter("regno", noPlat)
            .addBodyParameter("custno", custIdVal)
            .addBodyParameter("prodtyp", prodIdVal)
            .addBodyParameter("remark", name)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact",response!!.toString())

                    Tools.showSuccess(this@NewTransactActivity, response.getString("message"))
                }

                override fun onError(anError: ANError?) {
                    binding.layoutProgress.progressOverlay.visibility = GONE

                    val errorBody = JSONObject(anError!!.errorBody)
                    val error = errorBody.getString("message")
                    val data = errorBody.getString("data")

                    Tools.showError(this@NewTransactActivity, "$error $data")
                }

            })
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

    private fun populateCustomers() {

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(ApiEndPoint.listCustomer)
            .addHeaders("token", session.token)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact", response!!.toString())

                    val datas = response.getJSONArray("data")
                    val custId: MutableList<String> = ArrayList()
                    val customers: MutableList<String> = ArrayList()

                    for (i in 0 until datas.length()) {

                        custId.add(datas.getJSONObject(i).getString("CUSTNO"))
                        customers.add(datas.getJSONObject(i).getString("FULLNM")+"-"
                                +datas.getJSONObject(i).getString("FADR") )
                    }

                    val adapterArea: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(applicationContext, android.R.layout.simple_list_item_1,
                            customers as List<Any?>
                        )
                    binding.dropdownPelanggan.setAdapter(adapterArea)
                    binding.dropdownPelanggan.setOnItemClickListener { adapterView, view, i, l ->
                        //Toast.makeText(applicationContext,rpaId[i].toString(), Toast.LENGTH_LONG).show()
                        custIdVal = custId[i]
                    }
                }

                override fun onError(anError: ANError?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact", anError!!.message.toString())

                    val errorBody = JSONObject(anError.errorBody)

                    val error = errorBody.getString("message")

                    Tools.showError(this@NewTransactActivity, error)

                }

            })
    }

    private fun populateProds() {

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(ApiEndPoint.listProd)
            .addHeaders("token", session.token)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact", response!!.toString())

                    val datas = response.getJSONArray("data")
                    val prodId: MutableList<String> = ArrayList()
                    val products: MutableList<String> = ArrayList()

                    for (i in 0 until datas.length()) {

                        prodId.add(datas.getJSONObject(i).getString("PRODTYP"))
                        products.add(datas.getJSONObject(i).getString("PRODTYP")+"-"
                                +datas.getJSONObject(i).getString("PRODDES") )
                    }

                    val adapterArea: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(applicationContext, android.R.layout.simple_list_item_1,
                            products as List<Any?>
                        )
                    binding.dropdownProduct.setAdapter(adapterArea)
                    binding.dropdownProduct.setOnItemClickListener { adapterView, view, i, l ->
                        //Toast.makeText(applicationContext,rpaId[i].toString(), Toast.LENGTH_LONG).show()
                        prodIdVal = prodId[i]
                    }
                }

                override fun onError(anError: ANError?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact", anError!!.message.toString())

                    val errorBody = JSONObject(anError.errorBody)

                    val error = errorBody.getString("message")

                    Tools.showError(this@NewTransactActivity, error)

                }

            })
    }
}