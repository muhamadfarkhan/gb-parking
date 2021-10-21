package net.admission.api

class ApiEndPoint {
    companion object{

        private const val SERVER = "http://api-admission.farkhan.net"

        const val login = "$SERVER/login"
        const val profile = "$SERVER/profile"
        const val storeTransact = "$SERVER/tr-store"
        const val listTransact = "$SERVER/tr-list"
        const val listPelanggan = "$SERVER/pelanggan-list"

    }
}