package ru.kcoder.currency.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kcoder.currency.presentation.exchange.CurrencyExchangeViewModel
import ru.kcoder.currency.presentation.select.SelectCurrencyViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val selectCurrencyViewModelProvider: Provider<SelectCurrencyViewModel>,
    private val currencyExchangeViewModelProvider: Provider<CurrencyExchangeViewModel>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SelectCurrencyViewModel::class.java) -> selectCurrencyViewModelProvider.get() as T
            modelClass.isAssignableFrom(CurrencyExchangeViewModel::class.java) -> currencyExchangeViewModelProvider.get() as T
            else -> throw Throwable("View model of class $modelClass is not supported by factory")
        }
    }
}