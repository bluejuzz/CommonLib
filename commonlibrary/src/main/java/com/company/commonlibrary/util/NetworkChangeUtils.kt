package com.company.commonlibrary.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import com.blankj.utilcode.util.LogUtils

import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.Utils

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/21 18:09
 * @des 网络变化监听工具类
 */
object NetworkChangeUtils {

    private var mChangeListeners: MutableMap<String, NetworkChangeListener>? = mutableMapOf()
    private var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    /**
     * 注册变化监听
     *
     * @param changeListener 回调
     */
    fun registerListener(changeListener: NetworkChangeListener) {

        //刚注册的时候收到回调
        if (this.mChangeListeners?.containsKey(changeListener.getTag()) == false) {
            changeListener.callback(NetworkUtils.isConnected())
            LogUtils.i(changeListener.getTag() + "--添加网络监听")
        }

        mChangeListeners?.put(changeListener.getTag(), changeListener)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val connectivityManager =
                    Utils.getApp().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            connectivityManager.requestNetwork(
                    NetworkRequest.Builder().build(),
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            super.onAvailable(network)
                            mChangeListeners?.forEach {
                                it.value.callback(true)
                            }
                        }

                        override fun onLost(network: Network) {
                            super.onLost(network)
                            mChangeListeners?.forEach {
                                it.value.callback(false)
                            }
                        }
                    })
        } else {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
            mNetworkChangeReceiver = NetworkChangeReceiver()
            Utils.getApp().registerReceiver(mNetworkChangeReceiver, intentFilter)
        }
    }

    /**
     * 移除网络变化广播
     */
    fun unRegisterListener(tag: String) {
        mChangeListeners?.getValue(tag)?.let {
            LogUtils.i(it.getTag() + "--移除网络监听")
            mChangeListeners?.remove(it.getTag())
        }


    }

    /**
     * 销毁网络监听
     */
    fun onDestroy() {
        mChangeListeners?.clear()
        mChangeListeners = null
        if (mNetworkChangeReceiver != null) {
            Utils.getApp().unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
    }

    /**
     * 网络变化回调
     */
    interface NetworkChangeListener {
        fun getTag(): String

        /**
         * 回调
         *
         * @param isConnected 是否连接
         */
        fun callback(isConnected: Boolean)
    }

    @Suppress("DEPRECATION")
    private class NetworkChangeReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                val connectivityManager =
                        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isAvailable) {
                    mChangeListeners?.forEach {
                        it.value.callback(true)
                    }
                } else {
                    mChangeListeners?.forEach {
                        it.value.callback(false)
                    }
                }
            }
        }
    }
}
