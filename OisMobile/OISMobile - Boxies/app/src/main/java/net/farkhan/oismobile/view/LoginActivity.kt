package net.farkhan.oismobile.view

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView

import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.l.kotlin.api.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_login.*
import net.farkhan.oismobile.R
import net.farkhan.oismobile.utils.PrefHelper
import org.jetbrains.anko.startActivity

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : Activity() {

    private val authService by lazy {
        ApiService.create(this@LoginActivity)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        checkImei()

        email_sign_in_button.setOnClickListener { attemptLogin() }

        setup.setOnClickListener { startActivity<SetupActivity>() }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkImei() {
        if (ContextCompat.checkSelfPermission(
                        this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_PHONE_STATE)) {
            }
            else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE),
                    2) } }

        try{
            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val IMEI = tm.deviceId
            if (IMEI != null)
                Toast.makeText(this, "IMEI number: " + IMEI,
                        Toast.LENGTH_LONG).show()
            val prefHelper = PrefHelper(this@LoginActivity)

            prefHelper.imei = IMEI

        }catch (ex:Exception){
            Log.e("",ex.message)
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {

        // Reset errors.
        email.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false
        var focusView: View? = null


        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            checkLogin(emailStr,passwordStr)
        }
    }

    private fun checkLogin(username: String, password: String) {

        val prefHelper = PrefHelper(this@LoginActivity)

        val u: String = username
        val p: String = password
        val imei = prefHelper.imei

        authService.login(u,p,imei)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if(result.result == "true"){

                                prefHelper.isLogin = true
                                prefHelper.userName = result.dataLogin!!.uSRNM
                                prefHelper.company = result.company
                                prefHelper.gateid = result.dataLogin.gATEID
                                startActivity<MainActivity>()
                                finish()
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

    }
}
