package ru.kcoder.currency.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.kcoder.currency.config.DateTimeProvider
import ru.kcoder.currency.data.error.CantLoadCurrencyException
import ru.kcoder.currency.data.network.ApiKeySource
import ru.kcoder.currency.data.network.CurrencyApi
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.data.storage.PersistentStorage
import ru.kcoder.currency.domain.model.Currency
import ru.kcoder.currency.domain.model.CurrencyPersistent
import ru.kcoder.currency.domain.repository.AllCurrenciesRepository
import ru.kcoder.currency.utils.zipToPair
import timber.log.Timber
import javax.inject.Inject

class AllCurrenciesRepositoryImpl @Inject constructor(
    private val api: CurrencyApi,
    private val mapper: Currency.Mapper,
    private val persistentStorage: PersistentStorage,
    private val keyValueStorage: KeyValueStorage,
    private val dateTimeProvider: DateTimeProvider,
) : AllCurrenciesRepository {

    override fun getAllCurrencies(): Single<List<Currency>> {
        return isNeedUpdateCurrencies()
            .zipToPair(getSavedCurrencies()).flatMap { (isNeedUpdate, persistentCurrencies) ->
                if (isNeedUpdate || persistentCurrencies.currencies.isEmpty()) {
                    loadCurrencies()
                } else {
                    Single.just(persistentCurrencies.currencies)
                }
            }
    }

    private fun loadCurrencies(): Single<List<Currency>> {
        return api.getAllSupportedCurrencies(ApiKeySource.API_KEY)
            .map { dto ->
                if (dto.success != true || dto.currencies.isNullOrEmpty()) {
                    throw CantLoadCurrencyException(dto.success, dto.error?.code, dto.error?.info)
                }
                mapper.map(dto.currencies)
            }.flatMap { currencies ->
                saveCurrencies(currencies)
                    .andThen(saveUpdateTime())
                    .toSingle { currencies }
            }
    }

    private fun isNeedUpdateCurrencies(): Single<Boolean> {
        return keyValueStorage.getLong(ALL_CURRENCIES_LOAD_TIME_KEY, 0L).map { lustUpdate ->
            dateTimeProvider.currentDateTime.time - lustUpdate > dateTimeProvider.cacheUpdateTime
        }
    }

    private fun getSavedCurrencies(): Single<CurrencyPersistent> {
        return persistentStorage.readFromStorage(
            ALL_CURRENCIES_PERSISTENT_KEY,
            CurrencyPersistent::class.java
        )
            .onErrorResumeNext { error ->
                Timber.e(error)
                Single.just(CurrencyPersistent(emptyList()))
            }
    }

    private fun saveUpdateTime(): Completable {
        return keyValueStorage.saveLong(
            ALL_CURRENCIES_LOAD_TIME_KEY,
            dateTimeProvider.currentDateTime.time
        )
    }

    private fun saveCurrencies(currencies: List<Currency>): Completable {
        return persistentStorage.writeToStorage(
            ALL_CURRENCIES_PERSISTENT_KEY,
            CurrencyPersistent(currencies)
        )
    }

    companion object {
        private const val ALL_CURRENCIES_LOAD_TIME_KEY = "ALL_CURRENCIES_LOAD_TIME_KEY"
        private const val ALL_CURRENCIES_PERSISTENT_KEY = "ALL_CURRENCIES_PERSISTENT_KEY"
    }

}