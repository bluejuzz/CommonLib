package com.company.commonlib.network


import androidx.fragment.app.Fragment
import com.company.commonlib.databinding.FragmentHttpRequestBinding
import com.company.commonlibrary.base.BaseVMFragment
import com.company.commonlibrary.base.BaseViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [HttpRequestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HttpRequestFragment : BaseVMFragment<BaseViewModel, FragmentHttpRequestBinding>() {


    override fun initView() {

    }

    override fun initData() {

    }

    companion object {


        fun newInstance(): HttpRequestFragment {
            return HttpRequestFragment()
        }
    }

}
