package net.farkhan.oismobile.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.l.kotlin.api.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_check_in.*
import kotlinx.android.synthetic.main.fragment_out_manual.*
import kotlinx.android.synthetic.main.fragment_out_manual.view.*

import net.farkhan.oismobile.R
import net.farkhan.oismobile.print.PrintOutBluetooth
import net.farkhan.oismobile.utils.PrefHelper
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.startActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OutManualFragment : Fragment() {

    var notrans: String = "notran"
    lateinit var username: String

    private val apiService by lazy {
        ApiService.create(activity)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_out_manual, container, false)

        view.save_button.setOnClickListener { checkOut() }

        return view
    }

    private fun checkOut() {

        notrans = notran.text.toString()

        if (notrans.isEmpty()) {
            longToast("Mohon input data No Transaksi")
        }else{
            /*
           * Generate bitmap from the text provided,
           * The QR code can be saved using other methods such as stream(), file(), to() etc.
           * Check out the GitHub README page for the same
           * (https://github.com/kenglxn/QRGen)
           * */
            startActivity<PrintOutBluetooth>("NOTRAN" to notrans)
        }
    }


}
