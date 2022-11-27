package net.admission.view.transact

import android.Manifest
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
import android.widget.Toast
import android.util.DisplayMetrics
import android.R
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception
import java.text.DateFormat
import java.text.NumberFormat
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresApi
import android.bluetooth.BluetoothAdapter

import android.bluetooth.BluetoothSocket
import android.content.res.Resources
import androidx.annotation.Nullable
import net.admission.utils.BluetoothUtil
import java.io.IOException
import java.text.ParseException
import net.admission.helper.ScannerActivity
import android.widget.RelativeLayout
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView
import net.admission.helper.SessionManagerApps


class NewTransactActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    private lateinit var currentDate: String
    private lateinit var toDay: String
    private lateinit var datetimeIn: String
    private lateinit var dueDate: String
    private lateinit var noTrans: String
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
    private lateinit var sessionApps: SessionManagerApps


    private lateinit var noPlatPrint: String
    private lateinit var namePrint: String
    private lateinit var notePrint: String
    private lateinit var periodPrint: String
    private lateinit var mScannerView: ZXingScannerView

    private val locale = Locale("id", "ID")
    private val df: DateFormat = SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale)
    private val nf: NumberFormat = NumberFormat.getCurrencyInstance(locale)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_Light)
        binding = ActivityNewTransactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)
        sessionApps = SessionManagerApps(this)
        binding.frameLayoutCamera.visibility = GONE

        initStatus()
        initComponent()
        initButton()
        initSearch()
        initToolbar()
    }

    private fun initSearch() {

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

        AndroidNetworking.post(sessionApps.apiServer+ApiEndPoint.detailTransact)
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
                    val startDt = data.getJSONObject(0).getString("STARTDT")
                    val endDt = data.getJSONObject(0).getString("ENDDT")


                    if(remark.contains("-")){
                        nameEdit = remark.split("-")[0]
                        noteEdit = remark.split("-")[1]
                    }else{
                        nameEdit = remark
                        noteEdit = remark
                    }

                    noPlatPrint = passno
                    namePrint = nameEdit
                    notePrint = noteEdit
                    periodPrint = "$startDt s/d $endDt"

                    binding.etPlatNo.setText(passno)
                    binding.etNote.setText(noteEdit)
                    binding.etUserName.setText(nameEdit)
                    binding.etPeriod.setText("$startDt s/d $endDt")
                    custIdVal = custId
                    prodIdVal = prodId
                    binding.dropdownPelanggan.setText(plgName)
                    binding.dropdownProduct.setText("$prodId - $prodName")

                    populateCustomers()
                    populateProds()
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

        val date = Date()
        var df = SimpleDateFormat("yyyy-MM-dd")
        val c1: Calendar = Calendar.getInstance()
        toDay = df.format(date)

        noTrans = binding.etNoTrans.text.toString()

    }

    private fun initButton() {
        binding.btnSubmitTransact.setOnClickListener {
            submitTransact()
        }
        binding.btnPrint.setOnClickListener {
            doPrint(binding.btnPrint)
        }

        binding.btnSearchTrans.setOnClickListener {
            searchTrans(binding.etNoTrans.text.toString())
        }

        binding.btnScanTrans.setOnClickListener {
            binding.frameLayoutCamera.visibility = VISIBLE

            mScannerView = ZXingScannerView(this)
            if (mScannerView != null) {
                mScannerView.resumeCameraPreview(this);
            }

            mScannerView.setAutoFocus(true)
            mScannerView.setResultHandler(this)
            binding.frameLayoutCamera.addView(mScannerView)

            mScannerView.startCamera()
            doRequestPermission()
        }
    }

    private fun initScannerView() {
        mScannerView = ZXingScannerView(this)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        binding.frameLayoutCamera.addView(mScannerView)
    }

    private fun doRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 100)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                initScannerView()
            }
            else -> {
                /* nothing to do in here */
            }
        }
    }

    private fun searchTrans(trans: String) {
        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.post(sessionApps.apiServer+ApiEndPoint.getTrans)
            .addHeaders("token", session.token)
            .addBodyParameter("no_trans", trans)
            .setPriority(Priority.MEDIUM)
            .setOkHttpClient(okHttpClient)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    binding.layoutProgress.progressOverlay.visibility = GONE

                    Log.d("transact",response!!.toString())
                    val status = response.getString("status")

                    if(status == "200"){

                        val data = response.getJSONObject("data")

                        datetimeIn = data.getString("DATETIMEIN")

                        binding.etPeriod.setText("$datetimeIn s/d ")

                    }else{
                        Tools.showInfo(this@NewTransactActivity, "Data tidak ditemukan")
                    }

                }

                override fun onError(anError: ANError?) {
                    binding.layoutProgress.progressOverlay.visibility = GONE

                    val errorBody = JSONObject(anError!!.errorBody)
                    val error = errorBody.getString("message")
                    val data = errorBody.getString("data")

                    Tools.showError(this@NewTransactActivity, "Data tidak ditemukan")
                }

            })
    }

    private fun doPrint(view: View?) {

        if(binding.etPeriod.text.toString() != "" ||
            binding.etPeriod.text.toString().isNotEmpty() ) {

            val bAdapter = BluetoothAdapter.getDefaultAdapter()

            val pairedDevices: Set<BluetoothDevice> = bAdapter.getBondedDevices()
            if (pairedDevices.size > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (device in pairedDevices) {
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    Log.d("bluetooth:", deviceName + "-" + deviceHardwareAddress)
                }
            }

            try {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                ) else {

                    var socket: BluetoothSocket? = null
                    val thisData = "\n" +
                            "U Parking\n" +
                            "RS Brawijaya Saharjo\n" +
                            "================================\n" +
                            "\n" +
                            "Register Inap (Parkir)\n" +
                            "Tanggal          :" + toDay + "\n" +
                            "Nopol            :" + binding.etPlatNo.text.toString() + "\n" +
                            "Nama             :" + binding.etUserName.text.toString() + "\n" +
                            "Keterangan       :" + binding.etNote.text.toString() + "\n" +
                            "Masa Berlaku     :\n" + binding.etPeriod.text.toString() + "\n" +
                            "\n" +
                            "\n" +
                            "\n" +
                            "\n"
                    val data: ByteArray = thisData.toByteArray()

                    //Get BluetoothAdapter

                    //Get BluetoothAdapter
                    val btAdapter: BluetoothAdapter = BluetoothUtil.getBTAdapter()
                    if (btAdapter == null) {
                        Toast.makeText(baseContext, "Open Bluetooth", Toast.LENGTH_SHORT).show()
                        return
                    }
                    // Get sunmi InnerPrinter BluetoothDevice
                    // Get sunmi InnerPrinter BluetoothDevice
                    val device: BluetoothDevice = BluetoothUtil.getDevice(btAdapter)
                    if (device == null) {
                        Toast.makeText(baseContext,
                            "Make Sure Bluetooth have InnterPrinter",
                            Toast.LENGTH_LONG).show()
                        return
                    }

                    try {
                        socket = BluetoothUtil.getSocket(device)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    try {
                        BluetoothUtil.sendData(data, socket)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            } catch (e: Exception) {
                Log.e("APP", "Can't print", e)
            }
        }else{
            Toast.makeText(baseContext,
                "Could not print, empty data",
                Toast.LENGTH_LONG).show()
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

        AndroidNetworking.post(sessionApps.apiServer+ApiEndPoint.storeTransact)
            .addHeaders("token", session.token)
            .addBodyParameter("factno", noPlat)
            .addBodyParameter("notran", binding.etNoTrans.text.toString())
            .addBodyParameter("passno", noPlat)
            .addBodyParameter("regno", noPlat)
            .addBodyParameter("custno", custIdVal)
            .addBodyParameter("usrnm", session.username)
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

                    doPrint(binding.btnPrint)
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
        binding.layoutProgress.progressOverlay.visibility = VISIBLE
        binding.layoutProgress.textLoading.text = "Getting Data Pelanggan"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(sessionApps.apiServer+ApiEndPoint.listCustomer)
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
                        ArrayAdapter<Any?>(applicationContext, R.layout.simple_spinner_dropdown_item ,
                            customers as List<Any?>
                        )

                    binding.dropdownPelanggan.setDropDownBackgroundResource(R.color.darker_gray)
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
        binding.layoutProgress.progressOverlay.visibility = VISIBLE
        binding.layoutProgress.textLoading.text = "Getting Data Product"

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()

        AndroidNetworking.get(sessionApps.apiServer+ApiEndPoint.listProd)
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
                        ArrayAdapter<Any?>(applicationContext, android.R.layout.simple_spinner_dropdown_item,
                            products as List<Any?>
                        )

                    binding.dropdownProduct.setDropDownBackgroundResource(R.color.darker_gray)
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

        var df1: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val startDate: Date

        try {
            startDate = df1.parse(datetimeIn)
            c1.time = startDate
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        currentDate = datetimeIn //df.format(date) // get current date here

        c1.add(Calendar.DAY_OF_YEAR, addDays.toInt())
        df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val resultDate = c1.time
        dueDate = df.format(resultDate)

        binding.etPeriod.setText("$currentDate s/d $dueDate")
    }

    override fun handleResult(rawResult: Result?) {
        binding.etNoTrans.setText(rawResult?.text)
        binding.frameLayoutCamera.visibility = GONE
        searchTrans(rawResult?.text.toString())
        mScannerView.resumeCameraPreview(this)
        binding.frameLayoutCamera.removeView(mScannerView)
    }
}