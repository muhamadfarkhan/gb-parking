package net.farkhan.oismobile.view.fragment


import android.app.AlertDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_check_in.view.*
import android.view.inputmethod.InputMethodManager
import com.jaredrummler.materialspinner.MaterialSpinner
import net.glxn.qrgen.android.QRCode
import org.jetbrains.anko.support.v4.longToast
import android.view.View.GONE
import com.l.kotlin.api.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import net.farkhan.oismobile.R
import net.farkhan.oismobile.R.string.notran
import net.farkhan.oismobile.print.PrintBluetooth
import net.farkhan.oismobile.utils.PrefHelper
import net.farkhan.oismobile.utils.Utils
import net.farkhan.oismobile.view.LoginActivity
import net.farkhan.oismobile.view.MainActivity
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.support.v4.startActivity
import android.content.DialogInterface
import android.graphics.Bitmap
import net.farkhan.oismobile.utils.Shelter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CheckInFragment : Fragment() {

    private var datetimeIn2: String? = null
    private var bitmap2: Bitmap? = null
    var vehicle_type: String = "Mobil Vallet"
    lateinit var username: String
    lateinit var notran: String
    lateinit var company: String
    lateinit var gateid: String
    lateinit var company2: String
    private var jenis2: String? = null
    lateinit var nopol2: String
    lateinit var notran2: String

    private val apiService by lazy {
        ApiService.create(activity)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_check_in, container, false)

        val spinner = view.findViewById(R.id.spinner_vehicle) as MaterialSpinner
        spinner.setItems("Mobil Vallet")
        spinner.setOnItemSelectedListener { view, position, id, item ->
            vehicle_type = item.toString() }

        val prefHelper = PrefHelper(context)

        val isLogin = prefHelper.isLogin
        username = prefHelper.userName
        gateid = prefHelper.gateid
        notran = "notran"
        company = prefHelper.company

        view.username.text = username+'/'+gateid

        if(!isLogin){
            startActivity<LoginActivity>()
            activity!!.finish()
        }

        view.next_button.setOnClickListener { printBarcode() }

        view.print_button_2.setOnClickListener {
            val prefHelper = PrefHelper(context)

            Shelter.img = bitmap2
            prefHelper.datetimeIn = datetimeIn2
            prefHelper.company = company2
            prefHelper.jenis = jenis2
            prefHelper.nopol = nopol2
            prefHelper.notran = notran2

            startActivity<PrintBluetooth>()
        }

        view.app_sub_name.text = company

        view.awal_button.setOnClickListener { awalProses(username) }
        view.akhir_button.setOnClickListener { akhirProses(username) }

        view.username.setOnClickListener {

            val builder = AlertDialog.Builder(context)

            builder.setTitle("Confirm")
            builder.setMessage("Are you sure to logout?")

            builder.setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                // Do nothing but close the dialog
                prefHelper.isLogin = false
                startActivity<LoginActivity>()
                activity!!.finish()

                dialog.dismiss()
            })

            val alert = builder.create()
            alert.show()


        }

        return view
    }

    private fun akhirProses(username: String) {
        apiService.akhir(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if(result.result == "true"){
                                longToast(result.msg.toString())
                            }else{
                                longToast(result.msg.toString())
                            }

                        },
                        { error ->

                            longToast( error.message.toString())
                        }
                )
    }

    private fun awalProses(username: String) {
        apiService.awal(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if(result.result == "true"){
                                longToast(result.msg.toString())
                            }else{
                                longToast(result.msg.toString())
                            }

                        },
                        { error ->

                            longToast( error.message.toString())
                        }
                )
    }

    private fun printBarcode() {

        view?.hideKeyboard(context!!.inputMethodManager)

        val nopol = nopol_edit_text.text.toString()

        if (nopol.isEmpty()) {
            longToast("Mohon input data Nomor Polisi")
        }else if(nopol.length > 20) {
            longToast("Inputan terlalu panjang")
        }else{
            /*
           * Generate bitmap from the text provided,
           * The QR code can be saved using other methods such as stream(), file(), to() etc.
           * Check out the GitHub README page for the same
           * (https://github.com/kenglxn/QRGen)
           * */
            app_sub_name.visibility = GONE

            insertDataIN(nopol, vehicle_type)
        }


    }

    private fun insertDataIN(nopol: String, veh: String) {
        // API GOES HERE

        apiService.insert(nopol, veh, gateid, username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if(result.result == "true"){
                                notran = result.nOTRAN!!
                                val bitmap = QRCode.from(notran).withSize(240, 240).bitmap()
                                generationImageView.setImageBitmap(bitmap)
                                Shelter.img = bitmap

                                bitmap2 = bitmap

                                val prefHelper = PrefHelper(context)
                                prefHelper.datetimeIn = result.dataInsert!!.dATETIMEIN
                                prefHelper.company = company
                                prefHelper.jenis = result.jenis
                                prefHelper.nopol = nopol
                                prefHelper.notran = result.nOTRAN

                                datetimeIn2 = result.dataInsert!!.dATETIMEIN
                                company2 = company
                                jenis2 = result.jenis
                                nopol2 = nopol
                                notran2 = result.nOTRAN

                                startActivity<PrintBluetooth>()
                            }else{
                                Snackbar.make(
                                        root_layout, // Parent view
                                        result.msg.toString(),
                                        Snackbar.LENGTH_LONG //
                                ).setAction(
                                        "Try Again",
                                        {

                                        }).show()
                            }

                        },
                        { error ->
                            Snackbar.make(
                                    root_layout, // Parent view
                                    error.message.toString(), // Message to show
                                    Snackbar.LENGTH_LONG //
                            ).setAction(
                                    "Try Again",
                                    {

                                    }).show()
                        }
                )

        // API GOES HERE




    }

    fun View.hideKeyboard(inputMethodManager: InputMethodManager) {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }


}
