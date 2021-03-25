package com.company.commonlibrary.bean

import com.blankj.utilcode.util.LogUtils
import com.company.commonlibrary.BuildConfig
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:49
 * @des
 */
open class BaseRequestBody {

    /**
     * 获取请求体
     */
    fun getRequestBody(): RequestBody {
        val content = Gson().toJson(this)
        val requestBody = content.toRequestBody(TYPE_JSON)
        if (BuildConfig.DEBUG) {
            LogUtils.d(content)
        }
        return requestBody
    }

    companion object {
        private val TYPE_FILE = "multipart/form-data".toMediaTypeOrNull()
        private val TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    }
}
