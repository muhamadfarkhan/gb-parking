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

        initButton()
        initToolbar()
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
        productType = binding.etProductCode.text.toString()

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
            productType.isBlank() -> {
                binding.etProductCode.error = "Please fill the blank"
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
            .addBodyParameter("custno", noPlat)
            .addBodyParameter("prodtyp", noPlat)
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

    private fun populatePelanggan() {

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(ApiEndPoint.listPelanggan)
            .addHeaders("token", session.token)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact", response!!.toString())

                    val datas = response.getJSONArray("areas")
                    val areaId: MutableList<String> = ArrayList()
                    val areas: MutableList<String> = ArrayList()

                    for (i in 0 until datas.length()) {

                        areaId.add(datas.getJSONObject(i).getString("id"))
                        areas.add(datas.getJSONObject(i).getString("name")+"-"
                                +datas.getJSONObject(i).getString("address") )
                    }

                    val adapterArea: ArrayAdapter<*> =
                        ArrayAdapter<Any?>(applicationContext, android.R.layout.simple_list_item_1,
                            areas as List<Any?>
                        )
                    binding.dropdownPelanggan.setAdapter(adapterArea)
                    binding.dropdownPelanggan.setOnItemClickListener { adapterView, view, i, l ->
                        //Toast.makeText(applicationContext,rpaId[i].toString(), Toast.LENGTH_LONG).show()
                        areaIdVal = areaId[i]
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