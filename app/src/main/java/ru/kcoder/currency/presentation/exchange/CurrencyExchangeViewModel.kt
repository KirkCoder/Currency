package ru.kcoder.currency.presentation.exchange

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import ru.kcoder.currency.config.RxSchedulers
import ru.kcoder.currency.domain.model.CurrencyAmount
import ru.kcoder.currency.domain.usecases.GetCurrenciesExchangeUseCase
import ru.kcoder.currency.domain.usecases.ObserveSelectedCurrencyUseCase
import ru.kcoder.currency.presentation.base.BaseViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CurrencyExchangeViewModel @Inject constructor(
    rxSchedulers: RxSchedulers,
    private val selectedCurrencyPresentationFormatter: SelectedCurrencyPresentation.Formatter,
    private val currencyAmountPresentationFormatter: CurrencyAmountPresentation.Formatter,
    private val getCurrenciesExchangeUseCase: GetCurrenciesExchangeUseCase,
    private val observeSelectedCurrencyUseCase: ObserveSelectedCurrencyUseCase
) : BaseViewModel(rxSchedulers) {

    val selectedCurrency: LiveData<SelectedCurrencyPresentation>
        get() = _selectedCurrency

    private val _selectedCurrency = MutableLiveData<SelectedCurrencyPresentation>()

    val currencyExchangeLiveData: LiveData<CurrenciesExchangePresentation>
        get() = _currencyExchangeLiveData

    private val _currencyExchangeLiveData = MutableLiveData<CurrenciesExchangePresentation>()

    val closeViewModel: LiveData<Any>
        get() = _closeViewModel

    private val _closeViewModel = MutableLiveData<Any>()

    private val searchDisposables = CompositeDisposable()

    private var lastAmount: CurrencyAmount? = null

    init {
        observeSelectedCurrency()
    }

    private fun observeSelectedCurrency() {
        observeSelectedCurrencyUseCase.execute()
            .map(selectedCurrencyPresentationFormatter::format)
            .schedule(
                onNext = { currency ->
                    _selectedCurrency.value = currency
                    recalculateExchangesIfAmountExist()
                },
                onError = Timber::e
            )
    }

    private fun recalculateExchangesIfAmountExist() {
        lastAmount?.let { calculateChanges(it.amount.toString()) }
    }

    fun calculateChanges(text: String) {
        val selectedCurrency = _selectedCurrency.value
        val amount = text.toBigDecimalOrNull()
        if (selectedCurrency != null && amount != null) {
            val lastAmount = CurrencyAmount(
                name = selectedCurrency.shortName,
                amount = amount
            )
            if (lastAmount == this.lastAmount) return
            this.lastAmount = lastAmount
            searchDisposables.clear()
            Observable.fromCallable {
                lastAmount
            }.debounce(SEARCH_DEBOUNCE_TIME_MS, TimeUnit.MILLISECONDS)
                .firstOrError()
                .flatMap(getCurrenciesExchangeUseCase::execute)
                .map(currencyAmountPresentationFormatter::format)
                .schedule(
                    onSuccess = { currenciesAmountPresentation ->
                        showCurrencies(currenciesAmountPresentation)
                    },
                    onSubscribe = { disposable ->
                        searchDisposables.add(disposable)
                        showProgress()
                    },
                    onError = { error ->
                        showError()
                        Timber.e(error)
                    }
                )
        } else {
            showEmptyCurrencies()
        }
    }

    private fun showCurrencies(currenciesAmountPresentation: List<CurrencyAmountPresentation>) {
        _currencyExchangeLiveData.value = CurrenciesExchangePresentation(
            currencies = currenciesAmountPresentation,
            showError = false,
            showProgress = false
        )
    }

    private fun showProgress() {
        _currencyExchangeLiveData.value = CurrenciesExchangePresentation(
            currencies = emptyList(),
            showError = false,
            showProgress = true
        )
    }

    private fun showError() {
        _currencyExchangeLiveData.value = CurrenciesExchangePresentation(
            currencies = emptyList(),
            showError = true,
            showProgress = false
        )
    }

    private fun showEmptyCurrencies() {
        _currencyExchangeLiveData.value = CurrenciesExchangePresentation(
            currencies = emptyList(),
            showError = false,
            showProgress = false
        )
    }

    fun close() {
        _closeViewModel.value = Any()
    }

    override fun onCleared() {
        super.onCleared()
        searchDisposables.clear()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_TIME_MS = 500L
    }
}