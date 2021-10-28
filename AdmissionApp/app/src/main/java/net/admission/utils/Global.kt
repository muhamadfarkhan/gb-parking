package net.admission.utils

import android.app.Application

public class Global : Application() {
    companion object {
        @JvmField
        var apiServer: String = "http://api-admission.farkhan.net"
    }
}