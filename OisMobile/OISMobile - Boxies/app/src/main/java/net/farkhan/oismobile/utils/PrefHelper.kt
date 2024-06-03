package net.farkhan.oismobile.utils

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager
import net.farkhan.oismobile.view.LoginActivity

class PrefHelper(context: Context?){
    companion object {
        val DEVELOP_MODE = false
        private val DEVICE_TOKEN = "data.source.prefs.DEVICE_TOKEN"
        private val IS_LOGIN = "false"
    }

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    // save device token
    var deviceToken = preferences.getString(DEVICE_TOKEN, "")
        set(value) = preferences.edit().putString(DEVICE_TOKEN,value).apply()

    // is login
       var isLogin = preferences.getBoolean(IS_LOGIN, false)
        set(value) = preferences.edit().putBoolean(IS_LOGIN,value).apply()

    var userName = preferences.getString("username", "")
        set(value) = preferences.edit().putString("username",value).apply()

    var gateid = preferences.getString("gateid", "")
        set(value) = preferences.edit().putString("gateid",value).apply()

    var imei = preferences.getString("IMEI", "")
        set(value) = preferences.edit().putString("IMEI",value).apply()

    var company = preferences.getString("COMPANY", "")
        set(value) = preferences.edit().putString("COMPANY",value).apply()

    var datetimeIn = preferences.getString("datetimeIn", "")
        set(value) = preferences.edit().putString("datetimeIn",value).apply()

    var apiServer = preferences.getString("apiServer", "http://192.168.1.41/api/public/index.php/")
        set(value) = preferences.edit().putString("apiServer",value).apply()


    var jenis = preferences.getString("jenisKend", "Motor")
        set(value) = preferences.edit().putString("jenisKend",value).apply()


    var nopol = preferences.getString("nopol", "")
        set(value) = preferences.edit().putString("nopol",value).apply()


    var notran = preferences.getString("notran", "")
        set(value) = preferences.edit().putString("notran",value).apply()

    var MID = preferences.getString("MID", "D024035711830001")
        set(value) = preferences.edit().putString("MID",value).apply()

    var TID = preferences.getString("MID", "03570005")
        set(value) = preferences.edit().putString("TID",value).apply()

    @SuppressLint("CommitPrefEdits")
    fun logout(){
        preferences.edit().clear()
        preferences.edit().apply()
    }

}