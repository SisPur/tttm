package com.example.orata.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkUtilities {
    private val TAG = NetworkUtilities::class.java.simpleName
    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    var api_address = "https://xxx/api/"
    const val BASE_URL = "https ://xxx/api/"
    const val SHIPMENTS = "v1/login"
    private const val TIMEOUT_CONNECTION_IN_SECONDS = 5 * 60 * 60
    private val API_LOGCAT_MODE = HttpLoggingInterceptor.Level.BODY
    // return new X509Certificate[0];

    // Install the all-trusting trust manager
    private val bypassSSLClientBuilder:

            // Create an ssl socket factory with our all-trusting manager
            OkHttpClient.Builder
        private get() {
            val builder = OkHttpClient.Builder()
            try {
                val trustAllcerts =
                    arrayOf<TrustManager>(
                        object : X509TrustManager {
                            @Throws(CertificateException::class)
                            override fun checkClientTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            @Throws(CertificateException::class)
                            override fun checkServerTrusted(
                                chain: Array<X509Certificate>,
                                authType: String
                            ) {
                            }

                            override fun getAcceptedIssuers(): Array<X509Certificate> {
                                // return new X509Certificate[0];
                                return arrayOf()
                            }
                        }
                    )

                // Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllcerts, SecureRandom())

                // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory = sslContext.socketFactory
                builder.connectTimeout(
                    TIMEOUT_CONNECTION_IN_SECONDS.toLong(),
                    TimeUnit.SECONDS
                )
                builder.writeTimeout(
                    TIMEOUT_CONNECTION_IN_SECONDS.toLong(),
                    TimeUnit.SECONDS
                )
                builder.readTimeout(
                    TIMEOUT_CONNECTION_IN_SECONDS.toLong(),
                    TimeUnit.SECONDS
                )
                builder.sslSocketFactory(
                    sslSocketFactory,
                    trustAllcerts[0] as X509TrustManager
                )
                builder.hostnameVerifier { hostname, session -> true }
                return builder
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return builder
        }

//    fun loadJsonFromAsset(
//        filename: String?,
//        context: Context
//    ): String? {
//        var json: String? = null
//        json = try {
//            val `is` = context.assets.open(filename!!)
//            val size = `is`.available()
//            val buffer = ByteArray(size)
//            `is`.read(buffer)
//            `is`.close()
//            String(buffer, "UTF-8")
//        } catch (ex: IOException) {
//            ex.printStackTrace()
//            return null
//        }
//        return json
//    }

    fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(
                    """
                        $line
                        
                        """.trimIndent()
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }

    private fun getOkHttpClientWithAuth(
        context: Context,
        url: String,
        token: String
    ): OkHttpClient? {
        return getOkHttpClientWithAuth(context, url, -1, token)
    }

    private fun getOkHttpClientWithAuth(
        context: Context,
        url: String,
        paramID: Int,
        token: String
    ): OkHttpClient? {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = API_LOGCAT_MODE
        var newUrl = url
        if (paramID > -1) {
            newUrl = newUrl.replace("{ID}", paramID.toString())
        }
        val fullUrl = newUrl
        okHttpClient =
            bypassSSLClientBuilder // new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder()
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Authorization", "Bearer $token")
                        .url(BASE_URL + fullUrl)
                        .build()
                    chain.proceed(request)
                }.build()
        return okHttpClient
    }

    fun getRetrofitShipmentList(context: Context, token: String): Retrofit? {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                getOkHttpClientWithAuth(
                    context,
                    SHIPMENTS,
                    token
                )
            )
            .build()
        return retrofit
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val status: Boolean
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var networkInfo: NetworkInfo? = null
            if (connectivityManager != null) {
                networkInfo = connectivityManager.activeNetworkInfo
            }
            status = networkInfo != null && networkInfo.state == NetworkInfo.State.CONNECTED
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return status
    }
}