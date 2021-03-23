package com.company.commonlibrary.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */
open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    fun <T> createMutableLiveData  (): MutableLiveData<T>{
        return MutableLiveData<T>()
    }

}
