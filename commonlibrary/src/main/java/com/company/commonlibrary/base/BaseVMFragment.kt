package com.company.commonlibrary.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding


/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */
abstract class BaseVMFragment<VM : BaseViewModel, VB : ViewBinding> : BaseFragment<VB>() {
    protected lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider.AndroidViewModelFactory(requireActivity().application).create(getVMClass()!!)
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