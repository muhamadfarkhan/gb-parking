package net.valet.utils

import android.app.Application

class Global : Application() {
    companion object {
        @JvmField
        var apiServer: String = "http://api-admission.farkhan.net"
    }
}