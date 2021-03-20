package com.company.commonlibrary.retrofit

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.company.commonlibrary.BuildConfig
import com.company.commonlibrary.bean.BaseResponse
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2019/7/24
 * @des
 */
abstract class HttpObserver<T> : Observer<ApiResponse<Any>> {
    override fun onChanged(t: ApiResponse<Any>?) {
        when (t) {
            is ApiSuccessResponse -> t.let { successResponse ->
                try {
                    val type = getType(0)
                    val toJson = Gson().toJson(successResponse.body)
                    val response = Gson().fromJson<T>(toJson, type)
                    if (response != null) {
                        if (response is BaseResponse<*>) {
                            val baseResponse = response as BaseResponse<*>
                            if (baseResponse.isSuccessful) {
                                onSuccess(response)
                                onFinish()
                            } else {
                                onFailed(CommonException(baseResponse.code, CommonException.E_BUSINESS, Exception(baseResponse.message)))
                                onFinish()
                            }
                        } else {
                            onSuccess(response)
                            onFinish()
                        }
                    } else {
                        onFailed(CommonException(-1, CommonException.E_BUSINESS, Exception("后台返回数据为空")))
                        onFinish()
                    }

                } catch (e: Exception) {
                    onFailed(CommonException(CommonException.E_BUSINESS, CommonException.E_BUSINESS, e))
                    onFinish()
                }
            }
            is ApiEmptyResponse -> {
                onFailed(CommonException(CommonException.E_EMPTY, CommonException.E_EMPTY, Exception("")))
                onFinish()
            }
            is ApiErrorResponse -> t.let {
                onFailed(CommonException(it.code, CommonException.E_SYSTEM, it.error))
                onFinish()
            }
        }

    }

    /**
     * 根据位置获取泛型类的类型
     *
     * @param position 位置
     * @return 类型
     */
    private fun getType(position: Int): Type? {
        return getSuperclassTypeParameter(javaClass, position)
    }

    /**
     * 获取类泛型的类型
     *
     * @param subclass 判定类
     * @param position 位置
     * @return 类型
     */
    private fun getSuperclassTypeParameter(subclass: Class<*>, position: Int): Type? {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType
        return `$Gson$Types`.canonicalize((parameterized.actualTypeArguments)[position])
    }

    /**
     * 请求成功
     *
     * @param response 返回泛型类
     */
    abstract fun onSuccess(response: T)

    /**
     * 请求失败 默认根据错误码进行提示
     *
     * @param e 异常信息
     */
    open fun onFailed(e: CommonException) {

        if (BuildConfig.DEBUG) {
            LogUtils.e(e.toString())
            ToastUtils.showShort(CommonException.getHttpExceptionMessage(code = e.code, t = e.throwable))
        } else {
            when(e.errorType){
                CommonException.E_BUSINESS ->{
                    if (e.code==CommonException.E_EMPTY) {
                        ToastUtils.showShort("请求成功，后台返回数据为空")
                    } else {
                        ToastUtils.showShort("code:${e.code} msg:${e.throwable.message}")
                    }
                }
                CommonException.E_SYSTEM ->{
                    ToastUtils.showShort(CommonException.getHttpExceptionMessage(code = e.code, t = e.throwable))
                }
                else ->{
                    LogUtils.e(e.toString())
                }
            }
        }
    }

    /**
     * 结束（请求成功失败都会回调）
     */
    abstract fun onFinish()
}