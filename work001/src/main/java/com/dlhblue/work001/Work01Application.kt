package com.dlhblue.work001

import com.company.commonlibrary.base.BaseApplication
import com.company.commonlibrary.retrofit.NetworkConfig
import com.company.commonlibrary.retrofit.RetrofitClient


/**
 * @description:
 * @author: dinglaihong(A03221)
 * @email: laihong.ding@casstime.com
 * @date: 3/18/21 1:45 PM
 */
class Work01Application : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        val config = NetworkConfig.getDefaultConfig()
        config.baseUrl = BuildConfig.BASE_URL
        RetrofitClient.init(config)
    }
}