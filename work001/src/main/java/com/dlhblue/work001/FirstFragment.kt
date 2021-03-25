package com.dlhblue.work001

import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.SPUtils
import com.company.commonlibrary.base.BaseVMFragment
import com.company.commonlibrary.base.BaseViewModel
import com.company.commonlibrary.bean.BaseRequestBody
import com.company.commonlibrary.retrofit.ApiResponse
import com.company.commonlibrary.retrofit.CommonException
import com.company.commonlibrary.retrofit.HttpApi
import com.company.commonlibrary.retrofit.HttpObserver
import com.dlhblue.work001.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

class FirstFragment : BaseVMFragment<BaseViewModel, FragmentFirstBinding>() {


    override fun initView() {
        BusUtils.register(this)
        mViewBinding.submit.setOnClickListener {
            //showLoading("提交中。。。")
            val requestBody = BaseRequestBody().getRequestBody()
            HttpApi.post("https://http.aismono.net/mono-biz-app/educationclass/getCardList", requestBody)
                    .observe(this, object : HttpObserver<ApiResponse<String>>() {
                        override fun onSuccess(response: ApiResponse<String>) {
                            showMessage(GsonUtils.toJson(response))
                        }

                        override fun onFailed(e: CommonException) {
                            super.onFailed(e)
                        }

                        override fun onFinish() {
                            //hideLoading()
                        }

                    })

        }

    }

    @BusUtils.Bus(tag = "TAG_REFRESH")
    fun onRefresh(data: List<String>) {
        mViewBinding.phone.text = data[0]
        mViewBinding.host.text = data[1]

    }


    override fun onDestroyView() {
        super.onDestroyView()
        BusUtils.unregister(this)
    }



    override fun initData() {
        mViewBinding.phone.text = SPUtils.getInstance("work01").getString("phone", "未设置手机号码，点击右上角设置")
        mViewBinding.host.text = SPUtils.getInstance("work01").getString("host", "未设置上传地址，点击右上角设置")

    }
}