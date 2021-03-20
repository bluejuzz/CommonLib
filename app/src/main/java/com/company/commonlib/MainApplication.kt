package com.company.commonlib




import com.company.commonlibrary.base.BaseApplication
import com.company.commonlibrary.retrofit.LiveDataCallAdapterFactory
import com.company.commonlibrary.retrofit.NetworkConfig


import com.company.commonlibrary.retrofit.RetrofitClient

/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2019/7/6
 * @des
 */
class MainApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()

        val config = NetworkConfig.getDefaultConfig()
        config.baseUrl = BuildConfig.BASE_URL
        RetrofitClient.init(config)
    }

}