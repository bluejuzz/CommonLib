package com.company.commonlib.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ToastUtils
import com.company.commonlib.databinding.ActivityNetworkBinding
import com.company.commonlibrary.base.BaseVMActivity
import com.company.commonlibrary.retrofit.DownloadListener
import com.company.commonlibrary.retrofit.HttpApi
import com.company.commonlibrary.retrofit.HttpObserver
import com.tbruyelle.rxpermissions2.RxPermissions


class NetworkActivity : BaseVMActivity<NetworkViewModel, ActivityNetworkBinding>() {


    override fun initView() {

        mViewBinding.httpPost.setOnClickListener {
            showLoading("加载中")

            mViewModel.post()
            mViewModel.post.observe(this, object : HttpObserver<AismonoResponse<List<MyCardData.CardBean>>>() {
                        override fun onSuccess(response: AismonoResponse<List<MyCardData.CardBean>>) {
                            showMessage(GsonUtils.toJson(response))
                        }

                        override fun onFinish() {
                            hideLoading()
                        }

                    })

        }
        mViewBinding.httpGet.setOnClickListener {
            showLoading("加载中")
            mViewModel.get()
            mViewModel.get
                    .observe(this, object : HttpObserver<WanResponse<List<BannerData>>>() {
                        override fun onSuccess(response: WanResponse<List<BannerData>>) {
                            showMessage(GsonUtils.toJson(response))
                        }

                        override fun onFinish() {
                            hideLoading()
                        }

                    })
        }
        mViewBinding.httpDownload.setOnClickListener {
            RxPermissions(this)
                    //读写通讯录权限
                    .request(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe {
                        if (it) {
                            HttpApi.download("https://fga1.market.xiaomi.com/download/AppStore/0d52514bffe0141fc1effaf2f54d6da1928765483/cn.mc.shopping.apk", cacheDir.absolutePath + System.currentTimeMillis() + ".apk", object : DownloadListener {
                                override fun onStartDownload() {
                                    mViewBinding.httpDownload.text = "开始下载"
                                    mViewBinding.httpDownload.isEnabled = false
                                }

                                @SuppressLint("SetTextI18n")
                                override fun onProgress(progress: Int) {
                                    this@NetworkActivity.runOnUiThread {
                                        mViewBinding.httpDownload.text = "下载进度-->$progress"
                                    }
                                    Log.e("", "下载进度-->$progress")

                                }

                                override fun onFinishDownload() {
                                    mViewBinding.httpDownload.text = "下载完成"
                                    mViewBinding.httpDownload.isEnabled = true
                                }

                                @SuppressLint("SetTextI18n")
                                override fun onFail(errorInfo: String) {
                                    mViewBinding.httpDownload.text = "失败：$errorInfo"
                                    mViewBinding.httpDownload.isEnabled = true
                                }
                            })
                        } else {
                            ToastUtils.showShort("拒绝了存储权限")
                            finish()
                        }
                    }
        }
    }

    override fun initData() {
    }

    companion object {
        fun starter(context: Context) {
            context.startActivity(Intent(context, NetworkActivity::class.java))
        }
    }

}
