package com.company.commonlibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */

abstract class BaseFragment<VB : ViewBinding> : Fragment(), IFragment {

    protected lateinit var mViewBinding: VB
    private var mPopupView: BasePopupView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getVB(inflater, container)
        mViewBinding = getVB(layoutInflater, container)!!
        return mViewBinding.root
    }

    protected abstract fun initView()

    protected abstract fun initData()

    @MainThread
    override fun showLoading(tips: String) {
        if (mPopupView != null) {
            mPopupView!!.dismiss()
        }
        mPopupView = XPopup.Builder(context).asLoading(tips).show()
    }

    @MainThread
    override fun hideLoading() {
        if (mPopupView != null && mPopupView!!.isShow) {
            mPopupView!!.dismiss()
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun getVB(inflater: LayoutInflater, container: ViewGroup?): VB? {
        val cls = getClazz(1, this)
        try {
            val inflate: Method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.javaPrimitiveType)
            return inflate.invoke(null, inflater, container, false) as VB
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 根据位置获取泛型类的类型
     *
     * @param position 位置
     * @return 类型
     */
    protected fun getClazz(position: Int, any: Any): Class<*> {

        val type = any.javaClass.genericSuperclass as ParameterizedType?
        return type!!.actualTypeArguments[position] as Class<*>
    }
}
