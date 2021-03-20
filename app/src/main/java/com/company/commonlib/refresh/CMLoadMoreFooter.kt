package com.company.commonlib.refresh

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.company.commonlib.R
import com.scwang.smart.drawable.ProgressDrawable
import com.scwang.smart.refresh.layout.api.RefreshFooter
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.scwang.smart.refresh.layout.util.SmartUtil


/**
 * @description:
 * @author: dinglaihong(A03221)
 * @email: laihong.ding@casstime.com
 * @date: 3/12/21 9:59 AM
 */
class CMLoadMoreFooter : LinearLayout, RefreshFooter {
    private var mFooterText //标题文本
            : TextView? = null
    private var mStatusView //状态
            : ImageView? = null
    private var mProgressView //刷新动画视图
            : ImageView? = null
    private var mProgressDrawable //刷新动画
            : ProgressDrawable? = null

    private var mNoMoreData: Boolean = false


    constructor(context: Context?) : super(context) {
        this.initView(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        this.initView(context)
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.initView(context)
    }

    private fun initView(context: Context?) {
        gravity = Gravity.CENTER
        val inflate = LayoutInflater.from(context).inflate(R.layout.cm_loadmore_footer_layout, null)
        mFooterText = inflate.findViewById(R.id.tv_footer_title)
        mFooterText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        mFooterText?.visibility = VISIBLE //显示下拉箭头
        mProgressDrawable = ProgressDrawable()
        mProgressDrawable!!.setColor(0x4369ff)
        mStatusView = inflate.findViewById(R.id.img_footer_status)
        mProgressView = inflate.findViewById(R.id.img_footer_progress)
        mProgressView!!.setImageDrawable(mProgressDrawable)
        mStatusView!!.setBackgroundResource(R.mipmap.uikit_icon_refresh_blue_success)
        addView(inflate)
        minimumHeight = SmartUtil.dp2px(60f)
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {

            RefreshState.PullUpToLoad -> {
                mFooterText!!.text = "继续上拉加载更多~"
                mStatusView?.visibility = GONE
                mProgressView!!.visibility = GONE //隐藏动画
                //mArrowView?.animate()?.rotation(0F) //还原箭头方向
            }
            RefreshState.Loading -> {
                mStatusView?.visibility = GONE
                mFooterText!!.text = "数据正在加载中"
                mProgressView!!.visibility = VISIBLE //显示加载动画
                //mArrowView?.visibility = GONE //隐藏箭头
            }
            RefreshState.ReleaseToLoad -> {
                mFooterText!!.text = "取消加载更多~"
                //mArrowView?.animate()?.rotation(180F) //显示箭头改为朝上
            }
            else ->{}

        }
    }

    override fun getView() = this

    override fun getSpinnerStyle(): SpinnerStyle = SpinnerStyle.Translate

    override fun setPrimaryColors(vararg colors: Int) {

    }


    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {

    }

    override fun onMoving(isDragging: Boolean, percent: Float, offset: Int, height: Int, maxDragHeight: Int) {

    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {

    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        mProgressDrawable?.start();//开始动画
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        mProgressDrawable?.stop()//停止动画
        mProgressView!!.visibility = GONE //隐藏动画
        if (success) {
            mStatusView?.visibility = VISIBLE
            mFooterText?.text = "加载成功"
        } else {
            mFooterText?.text = "加载失败，请重试"
        }
        return 500//延迟500毫秒之后再弹回
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean = false
    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData
            if (noMoreData) {
                mProgressView!!.visibility = GONE //隐藏动画
                mStatusView?.visibility = GONE
                mFooterText?.text = "没有更多数据啦~"
            } else {
                mProgressView!!.visibility = GONE //隐藏动画
                mStatusView?.visibility = GONE
                mFooterText?.text = "上拉加载更多~"

            }

        }
        return true
    }

}