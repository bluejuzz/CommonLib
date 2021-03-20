package com.company.commonlib.contacts

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Base64
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.company.commonlib.R
import com.company.commonlib.databinding.ActivityContactsTestBinding
import com.company.commonlibrary.base.BaseVMActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class ContactsTestActivity : BaseVMActivity<ContactsModel, ActivityContactsTestBinding>() {
    private var adapter: BaseQuickAdapter<ContactBean, BaseViewHolder> = object : BaseQuickAdapter<ContactBean, BaseViewHolder>(R.layout.item_contacts, null) {

        override fun convert(holder: BaseViewHolder, item: ContactBean) {
            holder.setText(R.id.contact_name, item.name + " : " + item.telPhone)
            val imageView = holder.getView<ImageView>(R.id.contact_avatar)
            val bitmapTransform = RequestOptions.bitmapTransform(CircleCrop())
            try {
                val decode = Base64.decode(item.avatar, Base64.DEFAULT)
                Glide.with(this@ContactsTestActivity).load(decode).error(R.mipmap.avatar_def).apply(bitmapTransform).into(imageView)
            } catch (e: Exception) {
                Glide.with(this@ContactsTestActivity).load(R.mipmap.avatar_def).apply(bitmapTransform).error(R.mipmap.avatar_def).into(imageView)
            }
        }
    }

    override fun initView() {
        mViewBinding.contactW.setOnClickListener {
            showLoading("正在写入联系人")
            mViewModel.writeContacts().observe(this@ContactsTestActivity, Observer {
                hideLoading()
            })
        }
        mViewBinding.contactR.setOnClickListener {
            showLoading("正在读取联系人")
            mViewModel.readContacts().observe(this@ContactsTestActivity, Observer {
                hideLoading()
                adapter.setNewInstance(it as MutableList<ContactBean>?)
            })
        }
        mViewBinding.rcvContacts.layoutManager = LinearLayoutManager(this)
        mViewBinding.rcvContacts.adapter = adapter
    }

    @SuppressLint("AutoDispose", "CheckResult")
    override fun initData() {

        RxPermissions(this)
                //读写通讯录权限
                .request(android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.WRITE_CONTACTS)
                .safeSubscribe(object : Subscriber<Boolean>, io.reactivex.Observer<Boolean> {
                    override fun onNext(t: Boolean) {
                        if (t) {

                        } else {
                            ToastUtils.showShort("拒绝了联系人权限")
                            finish()
                        }
                    }

                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onSubscribe(s: Subscription) {

                    }

                    override fun onError(t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onComplete() {

                    }
                })

    }

    companion object {
        fun starter(context: Context) {
            context.startActivity(Intent(context, ContactsTestActivity::class.java))
        }
    }
}
