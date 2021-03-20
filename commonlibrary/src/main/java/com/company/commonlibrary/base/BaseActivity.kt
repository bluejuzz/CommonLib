package com.company.commonlibrary.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.BarUtils
import com.company.commonlibrary.util.NetworkChangeUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType


/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IActivity {

    protected lateinit var mViewBinding: VB
    private var mPopupView: BasePopupView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }*/
        if (!BarUtils.isStatusBarLightMode(this)) {
            BarUtils.setStatusBarLightMode(this, false)
        }
        if (this is NetworkChangeUtils.NetworkChangeListener) {
            NetworkChangeUtils.registerListener(this)
        }

        mViewBinding = getVB(layoutInflater)!!
        setContentView(mViewBinding.root)
        initView()
    }

    /**
     * 初始化布局
     */
    @MainThread
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    @MainThread
    protected abstract fun initData()


    @MainThread
    override fun showLoading(tips: String) {
        if (mPopupView != null) {
            mPopupView!!.dismiss()
        }
        mPopupView = XPopup.Builder(this).asLoading(tips).show()

    }

    @MainThread
    override fun hideLoading() {
        if (mPopupView != null) {
            mPopupView!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this is NetworkChangeUtils.NetworkChangeListener) {
            NetworkChangeUtils.unRegisterListener(localClassName)
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun getVB(inflater: LayoutInflater): VB? {
        try {
            val inflate = getClazz(1, this).getDeclaredMethod("inflate", LayoutInflater::class.java)
            return inflate.invoke(null, inflater) as VB
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
