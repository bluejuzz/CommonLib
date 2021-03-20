package com.company.commonlibrary.retrofit

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.company.commonlibrary.BuildConfig
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2018/08/13 13:52
 * @des
 */
class NetworkConfig {
    lateinit var application: Application

    var baseUrl: String = Uri.EMPTY.toString()

    var convertFactories: Array<retrofit2.Converter.Factory> = emptyArray()
    var callAdapterFactorys: Array<CallAdapter.Factory> = emptyArray()

    var interceptors: Array<Interceptor> = emptyArray()

    var networkInterceptors: Array<Interceptor> = emptyArray()

    var cacheStateSec: Long = (10 * 1024 * 1024).toLong()

    var readTimeOut: Long = 20

    var connectTimeOut: Long = 20
    var retryOnConnectionFailure: Boolean = true

    companion object {
        fun getDefaultConfig() :NetworkConfig{
            val config = NetworkConfig()
            config.baseUrl ="https://baseurl.com/"
            config.convertFactories = arrayOf(GsonConverterFactory.create(GsonFactory.getGson()))
            config.callAdapterFactorys = arrayOf(LiveDataCallAdapterFactory(),RxJava2CallAdapterFactory.create())
            config.readTimeOut =15
            config.connectTimeOut =15
            config.retryOnConnectionFailure =true
            config.interceptors = arrayOf()

            //  测试包打开日志
            val interceptor = HttpLoggingInterceptor()
            interceptor.level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            config.networkInterceptors = arrayOf(interceptor)
            config.cacheStateSec = (10 * 1024 * 1024).toLong()
            return config
        }
    }
}