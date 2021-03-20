package com.company.commonlibrary.retrofit

import android.util.Log

import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * @author dinglaihong
 * @email 18279727279@163.com
 * @date 2018/08/13 13:50
 * @des
 */
class DownloadResponseBody(private val responseBody: ResponseBody, private val downloadListener: DownloadListener?) : ResponseBody() {

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource as BufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                if (responseBody.contentLength() > 0) {
                    totalBytesRead += if (bytesRead != -1L) {
                        bytesRead
                    } else 0
                    Log.i("download", "read: " + (totalBytesRead * 100 / responseBody.contentLength()).toInt())
                    downloadListener?.apply {
                        if (bytesRead != -1L) {
                            onProgress((totalBytesRead * 100 / responseBody.contentLength()).toInt())
                        }
                    }
                }
                return bytesRead
            }
        }

    }
}
