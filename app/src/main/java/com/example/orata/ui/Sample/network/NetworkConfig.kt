package com.example.crud1.network

import com.example.crud1.model.ResultStatus
import com.example.orata.ui.Sample.model.ResultData
import com.example.orata.ui.Sample.model2.ResultProduk
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

object NetworkConfig {


    fun getInterceptor() : OkHttpClient {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return okHttpClient
    }

    fun getRetrofit(): Retrofit {
        val client: OkHttpClient.Builder = OkHttpClient.Builder()
        client.connectTimeout(15, TimeUnit.SECONDS)
        client.readTimeout(15, TimeUnit.SECONDS)
        client.writeTimeout(15, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl("http://tttm.tiketantrian.com/public/api/v01/")
            .client(getInterceptor())
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService() = getRetrofit().create(GeneralService::class.java)
    //fun getServiceProduk() = getRetrofit().create(ProdukService::class.java)
}

interface GeneralService{

    @FormUrlEncoded
    @POST("staff")
    fun addStaff(@Field("name") name : String,
                 @Field("hp") hp : String,
                 @Field("alamat") alamat : String) : Call<ResultStatus>

    @GET("staff")
    fun getDataStaff() : Call<ResultData>

    @GET("produk")
    fun getDataProduk() : Call<ResultProduk>

    @FormUrlEncoded
    @POST("staff/destroy")
    fun deleteStaff(@Field("id") id: String?) : Call<ResultStatus>

    @FormUrlEncoded
    @POST("staff/update")
    fun updateStaff(@Field("id") id: String,
                    @Field("name") name: String,
                    @Field("hp") hp : String,
                    @Field("alamat") alamat : String) : Call<ResultStatus>
}