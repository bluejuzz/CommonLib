package com.company.commonlibrary.retrofit


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.NetworkUtils
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.File
import java.net.ConnectException
import java.net.URLConnection


/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2018/08/13 13:52
 * @des
 */
interface HttpApi {
    
    companion object {
        private val TYPE_FILE = "multipart/form-data".toMediaTypeOrNull()
        private val TYPE_JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

        /**
         * 文件上传使用
         *
         * @param url              请求地址
         * @param paramsBody       参数,可使用[.getRequestBody]获取
         * @param file             文件
         */
        fun postFile(url: String, paramsBody: RequestBody, file: File): MutableLiveData<ApiResponse<Any>> {

            return when {
                !NetworkUtils.isConnected() -> {
                    MutableLiveData(ApiErrorResponse(CommonException.E_SYSTEM, ConnectException()))
                }
                else -> {
                    val requestBody = file.asRequestBody(judgeType(file.name))
                    val part = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    RetrofitClient.getFileRetrofit(null)
                            .create(RetrofitAppService::class.java)
                            .postFile(url, paramsBody, part)
                }
            }


        }

        /**
         * 普通POST请求使用
         *
         * @param url              请求地址
         * @param requestBody      请求体,可使用[.getRequestBody]获取
         */
        fun post(url: String, requestBody: RequestBody): MutableLiveData<ApiResponse<Any>> {
            //判断是否有网络连接，无网络连接直接返回失败
            return when {
                !NetworkUtils.isConnected() -> {
                    MutableLiveData(ApiErrorResponse(CommonException.E_SYSTEM, ConnectException()))
                }
                else -> {
                    RetrofitClient.getRetrofit()
                            .create(RetrofitAppService::class.java)
                            .post(url, requestBody)
                }
            }

        }

        /**
         * 普通GET请求使用
         *
         * @param url         请求地址
         * @param params      请求参数
         */
        fun get(url: String, params: Map<String, Any> = hashMapOf()): MutableLiveData<ApiResponse<Any>> {
            return when {
                !NetworkUtils.isConnected() -> {
                    MutableLiveData(ApiErrorResponse(CommonException.E_SYSTEM, ConnectException()))
                }
                else -> {
                    RetrofitClient.getRetrofit()
                            .create(RetrofitAppService::class.java)
                            .get(url, params)
                }
            }
        }


        /**
         * 文件下载
         *
         * @param downloadUrl      下载地址
         * @param saveFilepath     保存文件地址
         * @param downloadListener 下载回调
         */
        fun download(downloadUrl: String, saveFilepath: String, downloadListener: DownloadListener?) {
            when {
                !NetworkUtils.isConnected() -> downloadListener?.onFail("网络连接失败，请检查您的网络！")
                else -> {
                    downloadListener?.apply {
                        onStartDownload()
                        RetrofitClient.getFileRetrofit(this)
                                .create(RetrofitAppService::class.java)
                                .downloadFile(downloadUrl)
                                .map { t -> t.byteStream() }
                                .observeOn(Schedulers.computation())
                                .map { inputStream -> FileIOUtils.writeFileFromIS(saveFilepath, inputStream) }
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                                .safeSubscribe(object : Subscriber<Boolean>, Observer<Boolean> {
                                    override fun onSubscribe(d: Disposable) {
                                    }

                                    override fun onSubscribe(s: Subscription?) {
                                    }

                                    override fun onNext(isSuccessful: Boolean) = if (isSuccessful) {
                                        onFinishDownload()
                                    } else {
                                        onFail("save file error")
                                    }

                                    override fun onError(e: Throwable) {
                                        e.message?.let { onFail(it) }
                                    }

                                    override fun onComplete() {

                                    }
                                })
                    }

                }
            }
        }



        /**
         * 根据文件名来获取类型
         *
         * @param path 文件路径
         * @return 类型
         */
        private fun judgeType(path: String): MediaType? {
            val nameMap = URLConnection.getFileNameMap()
            val contentTypeFor = nameMap.getContentTypeFor(path)
            return if (contentTypeFor == null) TYPE_FILE else contentTypeFor.toMediaTypeOrNull()
        }
    }
}
