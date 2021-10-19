package net.admission.api

class ApiEndPoint {
    companion object{

        private const val SERVER = "http://api-admission.farkhan.net"

        const val login = "$SERVER/login"
        const val profile = "$SERVER/profile"

    }
}