package net.admission.helper

import android.content.Context
import android.content.SharedPreferences


class SessionManager(var c: Context) {
    var pref: SharedPreferences
    var editor: SharedPreferences.Editor
    var PRIVATE_MODE = 0

    //0 agar cuma bsa dibaca hp itu sendiri
    var PREF_NAME = "AdmissionAppPref"

    //membuat session login
    fun createLoginSession(token: String?) {
        editor.putString(KEY_TOKEN, token)
        editor.putBoolean(KEY_LOGIN, true)
        editor.commit()
        //commit digunakan untuk menyimpan perubahan
    }

    //mendapatkan token
    val token: String?
        get() = pref.getString(KEY_TOKEN, "")

    //cek login
    val isLogin: Boolean
        get() = pref.getBoolean(KEY_LOGIN, false)

    //cek status input
    var isCreate: Boolean
        get() = pref.getBoolean("isCreate", true)
        set(isCreate) {
            editor.putBoolean("isCreate", isCreate!!)
            editor.commit()
        }

    var idEditData: Int
        get() = pref.getInt("idEditData", 0)
        set(idEditData) {
            editor.putInt("idEditData", idEditData!!)
            editor.commit()
        }

    var stringEditData: String?
        get() = pref.getString("stringEditData", "")
        set(stringEditData) {
            editor.putString("stringEditData", stringEditData!!)
            editor.commit()
        }

    //logout user
    fun logout() {
        editor.clear()
        editor.commit()
    }

    fun setLocStart(lat: Double, lang: Double) {
        editor.putString("LocStart", "$lat,$lang")
        editor.commit()
    }

    val locStart: String?
        get() = pref.getString("LocStart", "")
    var idPelanggan: String?
        get() = pref.getString("idPelanggan", "")
        set(idPelanggan) {
            editor.putString("idPelanggan", idPelanggan)
            editor.commit()
        }
    var level: String?
        get() = pref.getString("level", "user")
        set(level) {
            editor.putString("level", level)
            editor.commit()
        }

    var tonaseId: String?
        get() = pref.getString("tonaseId", "0")
        set(tonaseId) {
            editor.putString("tonaseId", tonaseId)
            editor.commit()
        }

    var itemId: String?
        get() = pref.getString("itemId", "0")
        set(itemId) {
            editor.putString("itemId", itemId)
            editor.commit()
        }

    var itemName: String?
        get() = pref.getString("itemName", "0")
        set(itemName) {
            editor.putString("itemName", itemName)
            editor.commit()
        }

    var deviceLimit: String?
        get() = pref.getString("deviceLimit", "1")
        set(deviceLimit) {
            editor.putString("deviceLimit", deviceLimit)
            editor.commit()
        }
    var fCMToken: String?
        get() = pref.getString("fcmToken", "")
        set(fcmToken) {
            editor.putString("fcmToken", fcmToken)
            editor.commit()
        }
    var lastToken: String?
        get() = pref.getString("lastToken", "null")
        set(lastToken) {
            editor.putString("lastToken", lastToken)
            editor.commit()
        }
    var nama: String?
        get() = pref.getString("user_name", "")
        set(nama) {
            editor.putString("user_name", nama)
            editor.commit()
        }
    var managerName: String?
        get() = pref.getString("managerName", "")
        set(managerName) {
            editor.putString("managerName", managerName)
            editor.commit()
        }
    var managerPhone: String?
        get() = pref.getString("managerPhone", "")
        set(managerPhone) {
            editor.putString("managerPhone", managerPhone)
            editor.commit()
        }
    var plan: String?
        get() = pref.getString("plan", "")
        set(plan) {
            editor.putString("plan", plan)
            editor.commit()
        }
    var id: Int
        get() = pref.getInt("id", 0)
        set(id) {
            editor.putInt("id", id)
            editor.commit()
        }
    var groupId: Int
        get() = pref.getInt("GroupId", 0)
        set(GroupId) {
            editor.putInt("GroupId", GroupId)
            editor.commit()
        }
    var totalDevice: Int
        get() = pref.getInt("TotalDevice", 0)
        set(TotalDevice) {
            editor.putInt("TotalDevice", TotalDevice)
            editor.commit()
        }
    var limitDevice: Int
        get() = pref.getInt("LimitDevice", 0)
        set(LimitDevice) {
            editor.putInt("LimitDevice", LimitDevice)
            editor.commit()
        }
    var eventId: Int
        get() = pref.getInt("eventId", 0)
        set(eventId) {
            editor.putInt("eventId", eventId)
            editor.commit()
        }
    var namaPelanggan: String?
        get() = pref.getString("namaPelanggan", "")
        set(namaPelanggan) {
            editor.putString("namaPelanggan", namaPelanggan)
            editor.commit()
        }
    var email: String?
        get() = pref.getString("user_email", "")
        set(email) {
            editor.putString("user_email", email)
            editor.commit()
        }
    var isManager: String?
        get() = pref.getString("isManager", "false")
        set(isManager) {
            editor.putString("isManager", isManager)
            editor.commit()
        }
    var phone: String?
        get() = pref.getString("user_phone", "")
        set(phone) {
            editor.putString("user_phone", phone)
            editor.commit()
        }
    var alamat: String?
        get() = pref.getString("user_alamat", "")
        set(user_alamat) {
            editor.putString("user_alamat", user_alamat)
            editor.commit()
        }

    fun setIduser(id_user: String?) {
        editor.putString("id_user", id_user)
        editor.commit()
    }

    val idUser: String?
        get() = pref.getString("id_user", "")
    var userTenant: String?
        get() = pref.getString("user_tenant", "")
        set(user_tenant) {
            editor.putString("user_tenant", user_tenant)
            editor.commit()
        }
    var gcm: String?
        get() = pref.getString("gcm", "")
        set(gcm) {
            editor.putString("gcm", gcm)
            editor.commit()
        }

    //
    var speed: String?
        get() = pref.getString("speed", "0")
        set(speed) {
            editor.putString("speed", speed)
            editor.commit()
        }

    var odo: String?
        get() = pref.getString("odo", "0")
        set(odo) {
            editor.putString("odo", odo)
            editor.commit()
        }
    var address: String?
        get() = pref.getString("address", "")
        set(address) {
            editor.putString("address", address)
            editor.commit()
        }

    fun getlat(): String? {
        return pref.getString("lat", "0")
    }

    fun setLat(lat: String?) {
        editor.putString("lat", lat)
        editor.commit()
    }

    var lon: String?
        get() = pref.getString("lon", "0")
        set(lon) {
            editor.putString("lon", lon)
            editor.commit()
        }
    var imei: String?
        get() = pref.getString("imei", "0")
        set(imei) {
            editor.putString("imei", imei)
            editor.commit()
        }
    var imeiNotif: String?
        get() = pref.getString("imei_notif", "0")
        set(imei_notif) {
            editor.putString("imei_notif", imei_notif)
            editor.commit()
        }
    var finalNama: String?
        get() = pref.getString("final_nama", "0")
        set(final_nama) {
            editor.putString("final_nama", final_nama)
            editor.commit()
        }
    var finalPassword: String?
        get() = pref.getString("final_pwd", "0")
        set(final_pwd) {
            editor.putString("final_pwd", final_pwd)
            editor.commit()
        }
    var adminUsername: String?
        get() = pref.getString("AdminUsername", "0")
        set(AdminUsername) {
            editor.putString("AdminUsername", AdminUsername)
            editor.commit()
        }
    var adminPassword: String?
        get() = pref.getString("AdminPassword", "0")
        set(AdminPassword) {
            editor.putString("AdminPassword", AdminPassword)
            editor.commit()
        }
    var finalIdUser: String?
        get() = pref.getString("final_idUser", "0")
        set(final_idUser) {
            editor.putString("final_idUser", final_idUser)
            editor.commit()
        }
    var managerId: String?
        get() = pref.getString("managerId", "0")
        set(managerId) {
            editor.putString("managerId", managerId)
            editor.commit()
        }
    var finalTypeUser: String?
        get() = pref.getString("final_typeUser", "0")
        set(final_typeUser) {
            editor.putString("final_typeUser", final_typeUser)
            editor.commit()
        }
    var finalEmail: String?
        get() = pref.getString("final_email", "0")
        set(final_email) {
            editor.putString("final_email", final_email)
            editor.commit()
        }
    var objectName: String?
        get() = pref.getString("object_name", "0")
        set(object_name) {
            editor.putString("object_name", object_name)
            editor.commit()
        }
    var startDate: String?
        get() = pref.getString("start_date", "0")
        set(start_date) {
            editor.putString("start_date", start_date)
            editor.commit()
        }
    var endDate: String?
        get() = pref.getString("end_date", "0")
        set(end_date) {
            editor.putString("end_date", end_date)
            editor.commit()
        }
    var isLoadExpired: Boolean?
        get() = pref.getBoolean("load_expired", false)
        set(load_expired) {
            editor.putBoolean("load_expired", load_expired!!)
            editor.commit()
        }
    var fromNotification: Boolean?
        get() = pref.getBoolean("fromNotification", false)
        set(fromNotification) {
            editor.putBoolean("fromNotification", fromNotification!!)
            editor.commit()
        }
    var traffic: Boolean?
        get() = pref.getBoolean("traffic", false)
        set(traffic) {
            editor.putBoolean("traffic", traffic!!)
            editor.commit()
        }
    var isLoginAs: Boolean?
        get() = pref.getBoolean("isLoginAs", false)
        set(isLoginAs) {
            editor.putBoolean("isLoginAs", isLoginAs!!)
            editor.commit()
        }
    var icon: String?
        get() = pref.getString("icon_o", "")
        set(icon_o) {
            editor.putString("icon_o", icon_o)
            editor.commit()
        }
    var userId: String?
        get() = pref.getString("UserId", "")
        set(UserId) {
            editor.putString("UserId", UserId)
            editor.commit()
        }
    var username: String?
        get() = pref.getString("username", "")
        set(username) {
            editor.putString("username", username)
            editor.commit()
        }
    var fullname: String?
        get() = pref.getString("fullname", "")
        set(fullname) {
            editor.putString("fullname", fullname)
            editor.commit()
        }
    var rpaId: String?
        get() = pref.getString("rpaId", "")
        set(rpaId) {
            editor.putString("rpaId", rpaId)
            editor.commit()
        }
    var areaId: String?
        get() = pref.getString("areaId", "")
        set(areaId) {
            editor.putString("areaId", areaId)
            editor.commit()
        }
    var myPoint: String?
        get() = pref.getString("myPoint", "")
        set(myPoint) {
            editor.putString("myPoint", myPoint)
            editor.commit()
        }
    var urlArticle: String?
        get() = pref.getString("urlArticle", "")
        set(urlArticle) {
            editor.putString("urlArticle", urlArticle)
            editor.commit()
        }
    var urlProduct: String?
        get() = pref.getString("urlProduct", "")
        set(urlProduct) {
            editor.putString("urlProduct", urlProduct)
            editor.commit()
        }
    var adminContact: String?
        get() = pref.getString("admin_contact", "")
        set(admin_contact) {
            editor.putString("admin_contact", admin_contact)
            editor.commit()
        }
    var adminContact2: String?
        get() = pref.getString("admin_contact2", "")
        set(admin_contact2) {
            editor.putString("admin_contact2", admin_contact2)
            editor.commit()
        }
    var managerId2: String?
        get() = pref.getString("manager_id_2", "0")
        set(manager_id_2) {
            editor.putString("manager_id_2", manager_id_2)
            editor.commit()
        }
    var isAdminLogToUser: Boolean?
        get() = pref.getBoolean("isAdminLogToUser", false)
        set(isAdminLogToUser) {
            editor.putBoolean("isAdminLogToUser", isAdminLogToUser!!)
            editor.commit()
        }
    var useTail: Boolean?
        get() = pref.getBoolean("UseTail", true)
        set(UseTail) {
            editor.putBoolean("UseTail", UseTail!!)
            editor.commit()
        }
    val soundNotif: Boolean
        get() = pref.getBoolean("sound_notif", false)

    fun setSound_notif(sound_notif: Boolean?) {
        editor.putBoolean("sound_notif", sound_notif!!)
        editor.commit()
    }

    var push_notif: Boolean?
        get() = pref.getBoolean("push_notif", true)
        set(push_notif) {
            editor.putBoolean("push_notif", push_notif!!)
            editor.commit()
        }
    var uUID: String?
        get() = pref.getString("UUID", "null")
        set(UUID) {
            editor.putString("UUID", UUID)
            editor.commit()
        }

    companion object {
        private const val KEY_TOKEN = "tokenLogin"
        private const val KEY_LOGIN = "isLogin"
    }

    //konstruktor
    init {
        pref = c.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}