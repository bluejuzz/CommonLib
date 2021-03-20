package com.dlhblue.work001


import android.view.Menu
import android.view.MenuItem
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.company.commonlibrary.base.BaseVMActivity
import com.company.commonlibrary.base.BaseViewModel
import com.dlhblue.work001.databinding.ActivityMainBinding
import com.lxj.xpopup.XPopup
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

class MainActivity : BaseVMActivity<BaseViewModel, ActivityMainBinding>() {


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> {
                /*XPopup.Builder(this).asInputConfirm("设置号码", "", "输入号码", OnInputConfirmListener() {
                    if (!it.isNullOrBlank()) {
                        SPUtils.getInstance("work01").put("phone", it)
                    }
                }).show()*/
                XPopup.Builder(this)
                        .asCustom(CusPopView(this, object : CusPopView.ConfirmCallback {
                            override fun callback(phone: String?, host: String?) {
                                if (!phone.isNullOrBlank()) {
                                    SPUtils.getInstance("work01").put("phone", phone)
                                }
                                if (!host.isNullOrBlank()) {
                                    SPUtils.getInstance("work01").put("host", host)
                                }
                                BusUtils.post("TAG_REFRESH", listOf(phone,host))
                            }
                        })).show()
            }
        }
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun initView() {
        setSupportActionBar(mViewBinding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        mViewBinding.toolbar.setNavigationOnClickListener {
//            exitProcess(0)
            finish()
        }
        RxPermissions(this)
                //读写通讯录权限
                .request(android.Manifest.permission.READ_SMS)
                .safeSubscribe(object : Observer<Boolean>{
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(t: Boolean) {
                        if (t) {
                            ToastUtils.showShort("获得了短信权限")
                        } else {
                            ToastUtils.showShort("拒绝了短信权限")
                        }
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {

                    }
                })

    }

    override fun initData() {

    }
}