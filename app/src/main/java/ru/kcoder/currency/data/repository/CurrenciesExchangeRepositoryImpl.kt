package ru.kcoder.currency.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ru.kcoder.currency.config.DateTimeProvider
import ru.kcoder.currency.config.DefaultCurrencyProvider
import ru.kcoder.currency.data.error.CantLoadCurrencyExchangeException
import ru.kcoder.currency.data.network.ApiKeySource
import ru.kcoder.currency.data.network.CurrencyApi
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.data.storage.PersistentStorage
import ru.kcoder.currency.domain.model.CurrencyExchange
import ru.kcoder.currency.domain.repository.CurrenciesExchangeRepository
import ru.kcoder.currency.utils.zipToPair
import timber.log.Timber
import javax.inject.Inject

class CurrenciesExchangeRepositoryImpl @Inject constructor(
    private val api: CurrencyApi,
    private val keyValueStorage: KeyValueStorage,
    private val currencyExchangeMapper: CurrencyExchange.Mapper,
    private val persistentStorage: PersistentStorage,
    private val dateTimeProvider: DateTimeProvider,
    private val defaultCurrencyProvider: DefaultCurrencyProvider,
) : CurrenciesExchangeRepository {

    override fun getCurrenciesExchange(): Single<CurrencyExchange> {
        val defaultCurrency = defaultCurrencyProvider.defaultCurrencyName
        return isNeedUpdateCurrencies(defaultCurrency)
            .zipToPair(getSavedCurrencies(defaultCurrency))
            .flatMap { (isNeedUpdate, savedCurrency) ->
                if (isNeedUpdate || savedCurrency.isEmpty()) {
                    loadCurrencies(defaultCurrency)
                } else {
                    Single.just(savedCurrency)
                }
            }
    }

    private fun loadCurrencies(selectedCurrency: String): Single<CurrencyExchange> {
        return api.getCurrencyExchange(ApiKeySource.API_KEY, selectedCurrency).map { dto ->
            if (dto.success != true || dto.quotes.isNullOrEmpty()) {
                throw CantLoadCurrencyExchangeException(
                    dto.success,
                    dto.error?.code,
                    dto.error?.info
                )
            }
            currencyExchangeMapper.map(dto.quotes)
        }.flatMap { currencyExchange ->
            saveCurrenciesExchange(selectedCurrency, currencyExchange)
                .andThen(saveUpdateTime(selectedCurrency))
                .toSingle { currencyExchange }
        }
    }

    private fun isNeedUpdateCurrencies(selectedCurrency: String): Single<Boolean> {
        return keyValueStorage.getLong(selectedCurrency, 0L).map { lustUpdate ->
            dateTimeProvider.currentDateTime.time - lustUpdate > dateTimeProvider.cacheUpdateTime
        }
    }

    private fun getSavedCurrencies(selectedCurrency: String): Single<CurrencyExchange> {
        return persistentStorage.readFromStorage(
            selectedCurrency,
            CurrencyExchange::class.java
        )
            .onErrorResumeNext { error ->
                Timber.e(error)
                Single.just(
                    CurrencyExchange(
                        currencyNameWithAmount = emptyMap()
                    )
                )
            }
    }

    private fun saveUpdateTime(selectedCurrency: String): Completable {
        return keyValueStorage.saveLong(
            selectedCurrency,
            dateTimeProvider.currentDateTime.time
        )
    }

    private fun saveCurrenciesExchange(
        selectedCurrency: String,
        currencies: CurrencyExchange
    ): Completable {
        return persistentStorage.writeToStorage(
            selectedCurrency,
            currencies
        )
    }
}