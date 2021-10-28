package net.admission.api

class ApiEndPoint {
    companion object{

        private const val SERVER = ""

        const val login = "$SERVER/login"
        const val profile = "$SERVER/profile"
        const val storeTransact = "$SERVER/tr-store"
        const val listTransact = "$SERVER/tr-list"
        const val detailTransact = "$SERVER/tr-detail"
        const val listCustomer = "$SERVER/plg-list"
        const val listProd = "$SERVER/prod-list"

    }
}