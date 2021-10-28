package net.admission.view.transact

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import net.admission.adapter.AdapterListAnimation
import net.admission.api.ApiEndPoint
import net.admission.databinding.ActivityNewTransactBinding
import net.admission.helper.SessionManager
import net.admission.model.Transact
import net.admission.utils.Tools
import net.admission.view.LoginActivity
import net.admission.view.SplashScreenActivity
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class NewTransactActivity : AppCompatActivity() {
    private lateinit var currentDate: String
    private lateinit var dueDate: String
    private lateinit var passprddVal: String
    private lateinit var custIdVal: String
    private lateinit var prodIdVal: String
    private lateinit var noPlat: String
    private lateinit var name: String
    private lateinit var note: String
    private lateinit var nameEdit: String
    private lateinit var noteEdit: String
    private lateinit var productType: String
    private lateinit var binding: ActivityNewTransactBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTransactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        initStatus()
        initComponent()
        initButton()
        initToolbar()
    }

    private fun initStatus() {
        if(!session.isCreate){
            resetForm()
            getDetail(session.stringEditData)
        }
    }

    private fun getDetail(s: String?) {

        binding.layoutProgress.progressOverlay.visibility = VISIBLE
        binding.layoutProgress.textLoading.text = "Processing data"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(SplashScreenActivity().apiServer+ApiEndPoint.detailTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("passno", session.stringEditData)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE
                    Log.d("transact", response!!.toString())

                    val data = response.getJSONArray("data")

                    val passno = data.getJSONObject(0).getString("PASSNO")
                    val remark = data.getJSONObject(0).getString("REMARK")
                    val custId = data.getJSONObject(0).getString("CUSTNO")
                    val prodId = data.getJSONObject(0).getString("PRODTYP")
                    val prodName = data.getJSONObject(0).getString("PRODDES")
                    val plgName = data.getJSONObject(0).getString("FULLNM")

                    if(remark.contains("-")){
                        nameEdit = remark.split("-")[0]
                        noteEdit = remark.split("-")[1]
                    }else{
                        nameEdit = remark
                        noteEdit = remark
                    }

                    binding.etPlatNo.setText(passno)
                    binding.etNote.setText(noteEdit)
                    binding.etUserName.setText(nameEdit)
                    custIdVal = custId
                    prodIdVal = prodId
                    binding.dropdownPelanggan.setText(plgName)
                    binding.dropdownProduct.setText("$prodId - $prodName")

                }

                override fun onError(anError: ANError?) {

                    binding.layoutProgress.progressOverlay.visibility = GONE
                    Log.d("main-activity", anError!!.message.toString())

                    val errorBody = JSONObject(anError.errorBody)

                    val error = errorBody.getString("message")

                    Tools.showError(this@NewTransactActivity,error)

                }

            })
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
                confirmSubmit()
            }
        }
    }

    private fun confirmSubmit() {
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Apakah data sudah bener ?")
        builder.setPositiveButton("Ya"
        ) { _, _ ->
            goSubmit()
        }
        builder.setNegativeButton("cancel", null)
        builder.show()
    }

    private fun goSubmit() {
        binding.layoutProgress.progressOverlay.visibility = VISIBLE
        binding.layoutProgress.textLoading.text = "Processing data"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(SplashScreenActivity().apiServer+ApiEndPoint.storeTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("factno", noPlat)
            .addBodyParameter("passno", noPlat)
            .addBodyParameter("regno", noPlat)
            .addBodyParameter("custno", custIdVal)
            .addBodyParameter("prodtyp", prodIdVal)
            .addBodyParameter("name", name)
            .addBodyParameter("note", note)
            .addBodyParameter("startdt", currentDate)
            .addBodyParameter("enddt", dueDate)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact",response!!.toString())

                    resetForm()
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

    private fun resetForm() {
        binding.etPlatNo.setText("")
        binding.etUserName.setText("")
        binding.etPeriod.setText("")
        binding.etNote.setText("")
        binding.dropdownPelanggan.setText("")
        binding.dropdownProduct.setText("")
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

        AndroidNetworking.get(SplashScreenActivity().apiServer+ApiEndPoint.listCustomer)
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

        AndroidNetworking.get(SplashScreenActivity().apiServer+ApiEndPoint.listProd)
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
                    val passprdd: MutableList<String> = ArrayList()
                    val products: MutableList<String> = ArrayList()

                    for (i in 0 until datas.length()) {

                        prodId.add(datas.getJSONObject(i).getString("PRODTYP"))
                        passprdd.add(datas.getJSONObject(i).getString("PASSPRDD"))
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
                        passprddVal = passprdd[i]
                        changePeriod(passprdd[i])
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

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun changePeriod(addDays: String) {
        val date = Date()
        var df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val c1: Calendar = Calendar.getInstance()
        currentDate = df.format(date) // get current date here

        c1.add(Calendar.DAY_OF_YEAR, addDays.toInt())
        df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val resultDate = c1.time
        dueDate = df.format(resultDate)

        binding.etPeriod.setText("$currentDate s/d $dueDate")
    }
}