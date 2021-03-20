package com.company.commonlib.refresh

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.company.commonlib.R
import com.company.commonlib.databinding.ActivityHtmlTestBinding
import com.company.commonlibrary.base.BaseVMActivity
import com.company.commonlibrary.base.BaseViewModel
import com.company.commonlibrary.util.NetworkChangeUtils
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RefreshTestActivity : BaseVMActivity<BaseViewModel, ActivityHtmlTestBinding>(),NetworkChangeUtils.NetworkChangeListener {
    private var pageNo :Int = 1
    private   var  adapter : BaseQuickAdapter<String, BaseViewHolder>? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding.sr.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                pageNo = 1
                getData()
            }

            override fun onLoadMore(refreshLayout: RefreshLayout) {
                pageNo++
                getData()
            }
        })
        mViewBinding.rw.layoutManager = LinearLayoutManager(this)
        adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout._xpopup_adapter_text) {

            override fun convert(holder: BaseViewHolder, item: String) {
                holder.setText(R.id.tv_text, item)
            }
        }
        mViewBinding.rw.adapter  = adapter
        mViewBinding.sr.autoRefresh()
        showLoading("数据加载中。。。")



    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            val data = queryData()
            if (data!=null) {
                if (pageNo==1){
                    hideLoading()
                    mViewBinding.sr.finishRefresh()
                    adapter?.setNewInstance(data)
                }else{
                    adapter?.addData(data)
                    if (data.isEmpty()||data.size<5){
                        mViewBinding.sr.finishLoadMoreWithNoMoreData()
                    }else{
                        mViewBinding.sr.finishLoadMore()
                    }
                }
            } else {
                val view = TextView(this@RefreshTestActivity)
                view.text = "没有数据"
                adapter?.setEmptyView(view)
            }

        }
    }

   private suspend fun queryData() : MutableList<String>?{
       delay(2000)
        if (pageNo==4){
            return mutableListOf("1111","2222","3333")
        }

        return mutableListOf("mmmm", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy", "yyyy")
    }

    companion object {
        fun starter(context: Context) {
            context.startActivity(Intent(context, RefreshTestActivity::class.java))
        }
    }

    override fun initView() {

    }

    override fun initData() {

    }

    override fun getTag() = localClassName

    override fun callback(isConnected: Boolean) {
        LogUtils.i("000000")
    }

}
