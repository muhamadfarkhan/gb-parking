package id.mobile.nfcreader

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.nio.charset.Charset
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val TIME_FORMAT: DateFormat = SimpleDateFormat.getDateTimeInstance()
    private var mTagContent: LinearLayout? = null

    private var mAdapter: NfcAdapter? = null
    private var mPendingIntent: PendingIntent? = null
    private var mNdefPushMessage: NdefMessage? = null

    private var mDialog: AlertDialog? = null

    private val mTags: List<Tag> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }


    private fun newTextRecord(
        text: String,
        locale: Locale,
        encodeInUtf8: Boolean
    ): NdefRecord? {
        val langBytes: ByteArray = locale.getLanguage().toByteArray(Charset.forName("US-ASCII"))
        val utfEncoding: Charset =
            if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
        val textBytes: ByteArray = text.toByteArray(utfEncoding)
        val utfBit = if (encodeInUtf8) 0 else 1 shl 7
        val status = (utfBit + langBytes.size).toChar()
        val data = ByteArray(1 + langBytes.size + textBytes.size)
        data[0] = status.toByte()
        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
    }

}
