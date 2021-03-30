package ru.kcoder.currency.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val app: Application) {

    @Provides
    fun provideContext(): Context {
        return app
    }

    @Provides
    fun provideApplication(): Application {
        return app
    }
}