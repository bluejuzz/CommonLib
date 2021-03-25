package com.company.commonlib.network

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.company.commonlibrary.base.BaseViewModel
import com.company.commonlibrary.retrofit.ApiResponse
import com.company.commonlibrary.retrofit.HttpApi


/**
 * @description:
 * @author: dinglaihong(A03221)
 * @email: laihong.ding@casstime.com
 * @date: 3/25/21 10:10 AM
 */
class NetworkViewModel(app: Application) : BaseViewModel(app) {
    lateinit var post: MutableLiveData<ApiResponse<Any>>
    lateinit var get: MutableLiveData<ApiResponse<Any>>

    fun post() {
        val requestBody = AismonoRequestBody(phone = "18279727279").getRequestBody()
        post = HttpApi.post("https://http.aismono.net/mono-biz-app/educationclass/getCardList", requestBody)
    }

    fun get() {

        get = HttpApi.get("https://www.wanandroid.com/banner/json")
    }
}