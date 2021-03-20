package com.company.commonlibrary.retrofit


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2018/08/13 13:52
 * @des
 */
object RetrofitClient {
    private var networkConfig: NetworkConfig? = null

    /**
     * 网络请求初始化
     */
    fun init(config: NetworkConfig) {
        networkConfig = config
    }

    /**
     * 文件操作超时时间
     */
    private const val FILE_TIME_OUT = 60L


    fun getRetrofit(): Retrofit {
        val retrofit: Retrofit by lazy {
            if (networkConfig?.baseUrl.isNullOrBlank()) {
                throw Throwable("You must init networkConfig or baseUrl")
            }
            val builder: OkHttpClient.Builder = OkHttpClient.Builder().apply {
                networkConfig?.let { it ->
                    it.interceptors.forEach {
                        addInterceptor(it)
                    }
                    it.networkInterceptors.forEach {
                        addNetworkInterceptor(it)
                    }
                    connectTimeout(it.connectTimeOut, TimeUnit.SECONDS)
                    readTimeout(it.readTimeOut, TimeUnit.SECONDS)
                    writeTimeout(it.readTimeOut, TimeUnit.SECONDS)
                    retryOnConnectionFailure(it.retryOnConnectionFailure)
                }

            }


            Retrofit.Builder()
                    .apply {
                        networkConfig?.let { config ->

                            baseUrl(config.baseUrl)
                            client(builder.build())
                            config.convertFactories.forEach {
                                addConverterFactory(it)
                            }
                            config.callAdapterFactorys.forEach {
                                addCallAdapterFactory(it)
                            }
                        }
                    }
                    .build()
        }
        return retrofit
    }

    /**
     * 文件下载拦截器
     */
    private val DOWNLOAD_INTERCEPTOR = DownloadInterceptor(object : DownloadListener {
        override fun onStartDownload() {
            downloadListener?.onStartDownload()
        }

        override fun onProgress(progress: Int) {
            downloadListener?.onProgress(progress)
        }

        override fun onFinishDownload() {
            downloadListener?.onFinishDownload()
        }

        override fun onFail(errorInfo: String) {
            downloadListener?.onFail(errorInfo)
        }
    })

    private var downloadListener: DownloadListener? = null

    fun getFileRetrofit(downloadListener: DownloadListener?): Retrofit {
        val fileRetrofit: Retrofit by lazy {
            RetrofitClient.downloadListener = downloadListener
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                    .addInterceptor(DOWNLOAD_INTERCEPTOR)
                    .connectTimeout(FILE_TIME_OUT, TimeUnit.SECONDS)
                    .readTimeout(FILE_TIME_OUT, TimeUnit.SECONDS)
                    .writeTimeout(FILE_TIME_OUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
            getRetrofit().newBuilder().client(builder.build()).build()
        }
        return fileRetrofit
    }
}

