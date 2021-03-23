package com.company.commonlibrary.base

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.viewbinding.ViewBinding


/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */
abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewBinding> : BaseActivity<VB>() {
    protected lateinit var mViewModel: VM

    @MainThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(getVMClass()!!)
        initData()
    }

    @Suppress("UNCHECKED_CAST")
    private fun getVMClass(): Class<VM>? {
        try {
            return getClazz(0, this) as Class<VM>
        } catch (e: ClassCastException) {
        }

        return null

    }

}