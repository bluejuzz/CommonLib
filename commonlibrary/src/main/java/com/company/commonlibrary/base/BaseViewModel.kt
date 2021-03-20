package com.company.commonlibrary.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * @author dlh
 * @email 18279727279@163.com
 * @date 2019/05/05 13:48
 * @des
 */
open class BaseViewModel(app: Application) : AndroidViewModel(app) {
    /**
     * 在协程内执行suspend方法
     * @param context 协程上下文
     * @param start 协程启动模式
     * @param exceptionHandler 协程异常处理
     * @param block 需要执行的函数
     */
    fun suspendCoroutine(
            context: CoroutineContext = Dispatchers.Default,
            start: CoroutineStart = CoroutineStart.DEFAULT,
            exceptionHandler: CoroutineExceptionHandler,
            block: suspend CoroutineScope.() -> Unit,
    ) = CoroutineScope(context = context)
            .launch(
                    context = exceptionHandler,
                    start = start,
                    block = block
            )

}
