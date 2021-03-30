package ru.kcoder.currency.data.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import ru.kcoder.currency.config.DateTimeProvider
import ru.kcoder.currency.config.DefaultCurrencyProvider
import ru.kcoder.currency.data.dto.CurrenciesDto
import ru.kcoder.currency.data.error.CantLoadCurrencyExchangeException
import ru.kcoder.currency.data.network.CurrencyApi
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.data.storage.PersistentStorage
import ru.kcoder.currency.domain.model.CurrencyExchange
import java.util.*

class CurrenciesExchangeRepositoryImplTest {

    private val api = mock<CurrencyApi>()
    private val mapper = mock<CurrencyExchange.Mapper>()
    private val persistentStorage = mock<PersistentStorage>()
    private val keyValueStorage = mock<KeyValueStorage>()
    private val dateTimeProvider = mock<DateTimeProvider>()
    private val defaultCurrencyProvider = mock<DefaultCurrencyProvider>()
    private val date = mock<Date>()

    private val repository = CurrenciesExchangeRepositoryImpl(
        api,
        keyValueStorage,
        mapper,
        persistentStorage,
        dateTimeProvider,
        defaultCurrencyProvider
    )

    private val exchange = CurrencyExchange(
        mapOf("EUR" to "1234")
    )

    private val dto = CurrenciesDto(
        success = true,
        error = null,
        terms = null,
        source = null,
        privacy = null,
        timestamp = null,
        currencies = null,
        quotes = mapOf("EUR" to "1234")
    )

    private val invalidDto = dto.copy(success = false)

    @Before
    fun setUp() {
        whenever(defaultCurrencyProvider.defaultCurrencyName).thenReturn("USD")
        whenever(keyValueStorage.getLong(any(), any())).thenReturn(Single.just(10L))
        whenever(dateTimeProvider.currentDateTime).thenReturn(date)
        whenever(dateTimeProvider.cacheUpdateTime).thenReturn(0L)
        whenever(persistentStorage.readFromStorage<CurrencyExchange>(any(), any())).thenReturn(
            Single.just(exchange)
        )
    }

    @Test
    fun `Check that the cache is working correctly`() {
        whenever(date.time).thenReturn(9L)

        repository.getCurrenciesExchange()
            .test()
            .assertValue(exchange)
    }

    @Test
    fun `Checking that the cache time has expired`() {
        whenever(keyValueStorage.saveLong(any(), any())).thenReturn(Completable.complete())
        whenever(api.getCurrencyExchange(any(), any())).thenReturn(Single.just(dto))
        whenever(mapper.map(any())).thenReturn(exchange)
        whenever(date.time).thenReturn(19L)
        whenever(persistentStorage.writeToStorage<CurrencyExchange>(any(), any())).thenReturn(
            Completable.complete()
        )

        repository.getCurrenciesExchange()
            .test()
            .assertValue(exchange)
    }

    @Test
    fun `Checking invalid server answer`() {
        whenever(keyValueStorage.saveLong(any(), any())).thenReturn(Completable.complete())
        whenever(api.getCurrencyExchange(any(), any())).thenReturn(Single.just(invalidDto))
        whenever(mapper.map(any())).thenReturn(exchange)
        whenever(date.time).thenReturn(19L)
        whenever(persistentStorage.writeToStorage<CurrencyExchange>(any(), any())).thenReturn(
            Completable.complete()
        )

        repository.getCurrenciesExchange()
            .test()
            .assertError(CantLoadCurrencyExchangeException::class.java)
    }
}