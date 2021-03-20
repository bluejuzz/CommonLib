package com.dlhblue.work001

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.core.CenterPopupView


/**
 * @description:
 * @author: dinglaihong(A03221)
 * @email: laihong.ding@casstime.com
 * @date: 3/18/21 2:44 PM
 */
class CusPopView(context:Context, private var callback:ConfirmCallback?): CenterPopupView(context) {

    override fun initPopupContent() {
        super.initPopupContent()

        val phone = this.findViewById<EditText>(R.id.edit_phone)
        val host = this.findViewById<EditText>(R.id.edit_host)
        this.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
            dismissOrHideSoftInput()
        }
        phone.setText(SPUtils.getInstance("work01").getString("phone", ""))
        host.setText(SPUtils.getInstance("work01").getString("host", ""))

        this.findViewById<Button>(R.id.btn_confirm).setOnClickListener {
            val phone1 = phone?.text.toString()
            val host1 = host?.text.toString()
            if (phone1.isNotBlank() && host1.isNotBlank()) {
                callback?.callback(phone1, host1)
                dismiss()
            }else{
                ToastUtils.showShort("手机号和上传地址不能为空")
            }
        }
    }

    override fun getImplLayoutId(): Int {
        return R.layout.cus_pop_view
    }
    interface ConfirmCallback{
        fun callback(phone:String?,host :String?)
    }
}