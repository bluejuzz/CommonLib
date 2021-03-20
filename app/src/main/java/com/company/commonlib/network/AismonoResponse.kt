package com.company.commonlib.network

import com.company.commonlibrary.bean.BaseResponse

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/18 17:03
 * @des
 */
data class AismonoResponse<T>(
        var msg: String? = null,
) : BaseResponse<T>(message = msg)
