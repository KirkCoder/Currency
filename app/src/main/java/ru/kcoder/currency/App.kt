package ru.kcoder.currency

import android.app.Application
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import ru.kcoder.currency.di.AndroidModule
import ru.kcoder.currency.di.AppComponent
import ru.kcoder.currency.di.DaggerAppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        initDi()
        initRxErrorHandler()
    }

    private fun initDi() {
        appComponent = DaggerAppComponent.builder()
            .androidModule(AndroidModule(this))
            .build()

        appComponent.inject(this)
    }

    private fun initRxErrorHandler() {
        RxJavaPlugins.setErrorHandler { throwable ->
            Timber.tag("GlobalErrorHandler").e(throwable)

            if (throwable !is UndeliverableException && BuildConfig.DEBUG) {
                throw throwable
            }
        }
    }

    companion object {
        lateinit var instance: App
    }
}