package ru.kcoder.currency.domain.repository

import io.reactivex.Single
import ru.kcoder.currency.domain.model.CurrencyExchange

interface CurrenciesExchangeRepository {
    fun getCurrenciesExchange(): Single<CurrencyExchange>
}