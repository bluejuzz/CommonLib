package com.company.commonlib.network

import com.company.commonlibrary.bean.BaseResponse

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/18 13:50
 * @des
 */
 data class WanResponse<T>(private var errorCode: Int = 0,
                          private var errorMsg: String = "",
                          private var data: T? = null) : BaseResponse<T>(errorCode, errorMsg, data, isSuccessful = errorCode==0){}

