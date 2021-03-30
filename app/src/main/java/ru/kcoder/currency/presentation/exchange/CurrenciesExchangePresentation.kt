package ru.kcoder.currency.presentation.exchange

data class CurrenciesExchangePresentation(
    val currencies: List<CurrencyAmountPresentation>,
    val showError: Boolean,
    val showProgress: Boolean
)