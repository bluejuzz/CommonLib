package com.company.commonlib.refresh

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.company.commonlib.R
import com.scwang.smart.drawable.ProgressDrawable
import com.scwang.smart.refresh.layout.api.RefreshHeader
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
class CMRefreshHeader : LinearLayout, RefreshHeader {
    private var mHeaderText //标题文本
            : TextView? = null
    private var mArrowView //下拉箭头
            : ImageView? = null
    private var mStatusView //刷新状态
            : ImageView? = null
    private var mProgressView //刷新动画视图
            : ImageView? = null
    private var mProgressDrawable //刷新动画
            : ProgressDrawable? = null


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
        val inflate = LayoutInflater.from(context).inflate(R.layout.cm_refresh_header_layout, null)
        mHeaderText = inflate.findViewById(R.id.tv_header_title)
        mHeaderText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
        mHeaderText?.visibility = VISIBLE //显示下拉箭头
        mProgressDrawable = ProgressDrawable()
        mProgressDrawable!!.setColor(0x4369ff)
        mArrowView = inflate.findViewById(R.id.img_header_arrow)
        mProgressView = inflate.findViewById(R.id.img_header_progress)
        mStatusView = inflate.findViewById(R.id.img_header_status)
        mProgressView!!.setImageDrawable(mProgressDrawable)
        mStatusView!!.setImageResource(R.mipmap.uikit_icon_refresh_blue_success)
        mArrowView!!.setBackgroundResource(R.mipmap.uikit_icon_refresh_blue_down)
        addView(inflate)
        minimumHeight = SmartUtil.dp2px(60f)
    }

    override fun onStateChanged(refreshLayout: RefreshLayout, oldState: RefreshState, newState: RefreshState) {
        when (newState) {
            RefreshState.None, RefreshState.PullDownToRefresh -> {
                mHeaderText!!.text = "继续下拉可刷新~"
                mArrowView?.visibility = VISIBLE //显示下拉箭头
                mProgressView!!.visibility = GONE //隐藏动画
                mStatusView!!.visibility = GONE //隐藏动画
                mArrowView?.animate()?.rotation(0F) //还原箭头方向
            }
            RefreshState.Refreshing -> {
                mHeaderText!!.text = "正在刷新"
                mProgressView!!.visibility = VISIBLE //显示加载动画
                mArrowView?.visibility = GONE //隐藏箭头
                mStatusView?.visibility = GONE //隐藏箭头
            }
            RefreshState.ReleaseToRefresh -> {
                mHeaderText!!.text = "释放即可刷新哟~"
                mArrowView?.animate()?.rotation(180F) //显示箭头改为朝上
            }

            else -> {
            }

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
        mProgressView?.visibility = GONE
        mStatusView?.visibility = VISIBLE
        if (success) {
            mHeaderText?.text = "刷新成功"
        } else {
            mHeaderText?.text = "刷新失败，请重试"
        }
        return 500//延迟500毫秒之后再弹回
    }

    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {

    }

    override fun isSupportHorizontalDrag(): Boolean = false
}