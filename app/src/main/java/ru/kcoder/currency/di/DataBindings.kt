package ru.kcoder.currency.di

import dagger.Binds
import dagger.Module
import ru.kcoder.currency.config.*
import ru.kcoder.currency.data.repository.AllCurrenciesRepositoryImpl
import ru.kcoder.currency.data.repository.CurrenciesExchangeRepositoryImpl
import ru.kcoder.currency.data.repository.SelectCurrencyRepositoryImpl
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.data.storage.KeyValueStorageImpl
import ru.kcoder.currency.data.storage.PersistentStorage
import ru.kcoder.currency.data.storage.PersistentStorageImpl
import ru.kcoder.currency.domain.repository.AllCurrenciesRepository
import ru.kcoder.currency.domain.repository.CurrenciesExchangeRepository
import ru.kcoder.currency.domain.repository.SelectCurrencyRepository

@Module
abstract class DataBindings {

    @Binds
    abstract fun provideRxSchedulers(rxSchedulersImpl: RxSchedulersImpl): RxSchedulers

    @Binds
    abstract fun categoryRepository(categoryRepositoryImpl: AllCurrenciesRepositoryImpl): AllCurrenciesRepository

    @Binds
    abstract fun keyValueStorage(keyValueStorageImpl: KeyValueStorageImpl): KeyValueStorage

    @Binds
    abstract fun selectCurrencyRepository(keyValueStorageImpl: SelectCurrencyRepositoryImpl): SelectCurrencyRepository

    @Binds
    abstract fun persistentStorage(persistentStorageImpl: PersistentStorageImpl): PersistentStorage

    @Binds
    abstract fun dateTimeProvider(androidDateTimeProvider: AndroidDateTimeProvider): DateTimeProvider

    @Binds
    abstract fun currenciesExchangeRepository(currenciesExchangeRepositoryImpl: CurrenciesExchangeRepositoryImpl): CurrenciesExchangeRepository

    @Binds
    abstract fun defaultCurrencyProvider(defaultCurrencyProviderImpl: DefaultCurrencyProviderImpl): DefaultCurrencyProvider
}