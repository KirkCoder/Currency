package ru.kcoder.currency.domain.repository

import io.reactivex.Single
import ru.kcoder.currency.data.dto.CurrenciesDto
import ru.kcoder.currency.domain.model.Currency

interface AllCurrenciesRepository {
    fun getAllCurrencies(): Single<List<Currency>>
}