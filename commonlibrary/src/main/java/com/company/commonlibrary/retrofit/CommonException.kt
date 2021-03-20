package com.company.commonlibrary.retrofit

import com.google.gson.JsonSyntaxException


import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Locale

import retrofit2.HttpException

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/18 11:17
 * @des 自定义错误
 */
class CommonException
/**
 * 异常
 *
 * @param code      错误码
 * @param errorType 错误类型
 * @param throwable 异常
 */
(var code: Int, var errorType: Int = E_SYSTEM, var throwable: Throwable) : Exception(throwable.message) {
    companion object {

        fun getHttpExceptionMessage(code: Int, errorType: Int = E_SYSTEM, t: Throwable): String {
            val message: String = when (t) {
                is ConnectException -> "无法连接到服务器，请检查您的服务器或网络设置！"
                is HttpException -> "出错了，网络不好！"
                is UnknownHostException -> "找不到服务器，请检查您的服务器或网络设置！"
                is SocketTimeoutException -> "连接超时，请检查您的服务器或网络设置！"
                is NumberFormatException -> "数据包解析异常！"
                is JsonSyntaxException -> "Json数据解析异常！"
                is SocketException -> "连接服务器失败！"
                else -> t.message.toString()
            }
            return String.format(Locale.CHINESE, "code: %d msg: %s", code, message)
        }

        /**
         * 业务错误
         */
        const val E_BUSINESS = -1000

        /**
         * 系统错误
         */
        const val E_SYSTEM = -9999

        /**
         * 空数据
         */
        const val E_EMPTY = -9998
    }

    override fun toString(): String {
        return "CommonException(code=$code, errorType=$errorType, throwable=$throwable)"
    }


}
