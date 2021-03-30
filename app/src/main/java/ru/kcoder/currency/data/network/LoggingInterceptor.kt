package ru.kcoder.currency.data.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import ru.kcoder.currency.BuildConfig
import javax.inject.Inject


class LoggingInterceptor @Inject constructor() : Interceptor {

    private val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.intercept(chain)
        } else {
            with(chain) {
                proceed(request())
            }
        }
    }

}