package ru.kcoder.currency.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
class GsonModule {

    @Reusable
    @Provides
    fun provideGsonInstance(): Gson {
        return GsonBuilder().create()
    }
}