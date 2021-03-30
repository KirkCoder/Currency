package ru.kcoder.currency.data.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.kcoder.currency.config.DateTimeProvider
import ru.kcoder.currency.data.dto.CurrenciesDto
import ru.kcoder.currency.data.network.CurrencyApi
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.data.storage.PersistentStorage
import ru.kcoder.currency.domain.model.Currency
import ru.kcoder.currency.domain.model.CurrencyPersistent
import java.util.*


class AllCurrenciesRepositoryImplTest {

    private val api = mock<CurrencyApi>()
    private val mapper = mock<Currency.Mapper>()
    private val persistentStorage = mock<PersistentStorage>()
    private val keyValueStorage = mock<KeyValueStorage>()
    private val dateTimeProvider = mock<DateTimeProvider>()
    private val date = mock<Date>()

    private val repository = AllCurrenciesRepositoryImpl(
        api,
        mapper,
        persistentStorage,
        keyValueStorage,
        dateTimeProvider
    )

    private val currency = Currency("name", "description")

    private val dto = CurrenciesDto(
        success = true,
        error = null,
        terms = null,
        source = null,
        privacy = null,
        timestamp = null,
        currencies = mapOf("USD" to "123"),
        quotes = null
    )

    private val currenciesList = listOf(currency)

    private val currencyPersistent = CurrencyPersistent(
        currenciesList
    )

    @Before
    fun setUp() {
        whenever(keyValueStorage.getLong(any(), any())).thenReturn(Single.just(10L))
        whenever(dateTimeProvider.currentDateTime).thenReturn(date)
        whenever(persistentStorage.readFromStorage<CurrencyPersistent>(any(), any())).thenReturn(
            Single.just(currencyPersistent)
        )
    }

    @Test
    fun `Check that the cache is working correctly`() {
        whenever(date.time).thenReturn(9L)
        whenever(dateTimeProvider.cacheUpdateTime).thenReturn(0L)

        repository.getAllCurrencies()
            .test()
            .assertValue(currencyPersistent.currencies)
    }

    @Test
    fun `Checking that the cache time has expired`() {
        whenever(keyValueStorage.saveLong(any(), any())).thenReturn(Completable.complete())
        whenever(api.getAllSupportedCurrencies(any())).thenReturn(Single.just(dto))
        whenever(mapper.map(any())).thenReturn(currenciesList)
        whenever(date.time).thenReturn(19L)
        whenever(dateTimeProvider.cacheUpdateTime).thenReturn(0L)

        whenever(persistentStorage.writeToStorage<CurrencyPersistent>(any(), any())).thenReturn(
            Completable.complete()
        )

        repository.getAllCurrencies()
            .test()
            .assertValue(currenciesList)
    }
}