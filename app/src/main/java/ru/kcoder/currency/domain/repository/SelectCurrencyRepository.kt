package ru.kcoder.currency.domain.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface SelectCurrencyRepository {

    fun selectCurrency(name: String): Completable

    fun getSelectedCurrency(): Single<String>

    fun observeSelectedCurrency(): Observable<String>

    companion object {
        const val SELECTED_CURRENCY_KEY = "SELECTED_CURRENCY_KEY"
    }
}