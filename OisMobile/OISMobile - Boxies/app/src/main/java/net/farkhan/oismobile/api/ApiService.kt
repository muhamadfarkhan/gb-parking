package com.l.kotlin.api

import android.content.Context
import io.reactivex.Observable
import net.farkhan.oismobile.model.*
import net.farkhan.oismobile.utils.PrefHelper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface ApiService {


    @POST("login_p")
    @FormUrlEncoded
    fun login(@Field("email") username: String,@Field("password") password: String,@Field("imei") imei: String)
            :Observable<ResponseLogin>

    @POST("insert_p")
    @FormUrlEncoded
    fun insert(@Field("REGNO") REGNO: String,@Field("VEHCLASS") VEHCLASS: String,
               @Field("USRNME") USRNME: String,@Field("USERNAME") USERNAME: String)
            :Observable<ResponseInsert2>

    @GET("check")
    fun checkGet(@Query("NOTRAN") NOTRAN: String)
            :Observable<ResponseInsert>

    @POST("id")
    @FormUrlEncoded
    fun check(@Field("IDTransaction") IDTransaction: String,@Field("USERNAME") USERNAME: String)
            :Observable<ResponseCheck1>

    @POST("testAwal")
    @FormUrlEncoded
    fun awal(@Field("email") email: String)
            :Observable<ResponseInsert>

    @POST("testAkhir")
    @FormUrlEncoded
    fun akhir(@Field("email") email: String)
            :Observable<ResponseInsert>


    companion object {
            fun create(context: Context?): ApiService {


                val prefHelper = PrefHelper(context)

                val api = prefHelper.apiServer
                val apis:String

                if(api.isNullOrBlank()){
                    apis = "http://192.168.1.41/api/public/index.php/"
                }else{
                    apis = api
                }

                val retrofit = Retrofit.Builder()
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(apis)
//                        .baseUrl("http://parking.farkhan.net/public/")
                        .build()

                return retrofit.create(ApiService::class.java)
            }
        }

}