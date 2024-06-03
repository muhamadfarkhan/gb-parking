package net.farkhan.oismobile.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView
import me.dm7.barcodescanner.zxing.ZXingScannerView
import net.farkhan.oismobile.print.PrintOutBluetooth
import net.farkhan.oismobile.view.PrintOutActivity
import org.jetbrains.anko.support.v4.startActivity


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class CheckOutFragment : Fragment(), ZBarScannerView.ResultHandler {



    private lateinit var mScannerView: ZBarScannerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mScannerView = ZBarScannerView(activity)
        mScannerView.setAutoFocus(true)
        return mScannerView;
    }

    override fun handleResult(rawResult: Result) {

        //Toast.makeText(context,rawResult.contents,Toast.LENGTH_LONG).show()
        //Camera will stop after scanning result, so we need to resume the
        //preview in order scan more codes
        //mScannerView.resumeCameraPreview(this)

        startActivity<PrintOutBluetooth>("NOTRAN" to rawResult.contents.toString())
//        startActivity<PrintOutBluetooth>("NOTRAN" to rawResult.text.toString())
    }

    /*
    * It is required to start and stop camera in lifecycle methods
    * (onResume and onPause)
    * */
    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }


}
