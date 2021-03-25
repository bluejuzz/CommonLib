package com.company.commonlib

import com.alibaba.android.arouter.facade.annotation.Route
import com.company.commonlib.contacts.ContactsTestActivity
import com.company.commonlib.databinding.ActivityMainBinding
import com.company.commonlib.network.NetworkActivity
import com.company.commonlibrary.base.BaseVMActivity
import com.company.commonlibrary.base.BaseViewModel

import com.company.commonlibrary.util.NetworkChangeUtils

/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2019/7/6
 * @des
 */
@Route(path = "main/home")
class MainActivity : BaseVMActivity<BaseViewModel, ActivityMainBinding>(), NetworkChangeUtils.NetworkChangeListener {

    override fun initView() {
        mViewBinding.testHttp.setOnClickListener {
            NetworkActivity.starter(this)
        }
        mViewBinding.testCamerax.setOnClickListener {
            ContactsTestActivity.starter(this)
        }
        mViewBinding.testContacts.setOnClickListener {
            ContactsTestActivity.starter(this)
        }
    }

    override fun initData() {

    }

    override fun getTag() = localClassName

    override fun callback(isConnected: Boolean) {
        showMessage("网络变化，是否有网络连接：$isConnected")
    }


}

