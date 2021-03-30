package ru.kcoder.currency.presentation.select

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kcoder.currency.config.RxSchedulers
import ru.kcoder.currency.domain.usecases.GetAllCurrenciesUseCase
import ru.kcoder.currency.domain.usecases.SelectCurrencyUseCase
import ru.kcoder.currency.presentation.base.BaseViewModel
import ru.kcoder.currency.presentation.select.AllCurrenciesPresentation
import ru.kcoder.currency.presentation.select.CurrencyPresentation
import timber.log.Timber
import javax.inject.Inject

class SelectCurrencyViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val selectCurrencyUseCase: SelectCurrencyUseCase,
    private val currencyPresentationFormatter: CurrencyPresentation.Formatter,
    rxSchedulers: RxSchedulers
) : BaseViewModel(rxSchedulers) {

    val allCurrenciesLiveData: LiveData<AllCurrenciesPresentation>
        get() = _allCurrenciesLiveData

    private val _allCurrenciesLiveData = MutableLiveData<AllCurrenciesPresentation>()

    val closeLiveData: LiveData<Any>
        get() = _closeLiveData

    private val _closeLiveData = MutableLiveData<Any>()

    init {
        loadAllCurrencies()
    }

    fun loadAllCurrencies() {
        getAllCurrenciesUseCase.execute()
            .map(currencyPresentationFormatter::format)
            .schedule(
                onSuccess = { currencies ->
                    showCurrencies(currencies)
                },
                onSubscribe = {
                    showProgress()
                },
                onError = { error ->
                    showError()
                    Timber.e(error)
                }
            )
    }

    private fun showCurrencies(currencies: List<CurrencyPresentation>) {
        _allCurrenciesLiveData.value = AllCurrenciesPresentation(
            currencies = currencies,
            showProgress = false,
            showError = false
        )
    }

    private fun showProgress() {
        _allCurrenciesLiveData.value = AllCurrenciesPresentation(
            currencies = emptyList(),
            showProgress = true,
            showError = false
        )
    }

    private fun showError() {
        _allCurrenciesLiveData.value = AllCurrenciesPresentation(
            currencies = emptyList(),
            showProgress = false,
            showError = true
        )
    }

    fun selectCurrency(currencyPresentation: CurrencyPresentation) {
        selectCurrencyUseCase.execute(currencyPresentation.name)
            .schedule(
                onComplete = {
                    close()
                },
                onError = Timber::e
            )
    }

    fun close() {
        _closeLiveData.value = Any()
    }
}