package ru.kcoder.currency.presentation.select

data class AllCurrenciesPresentation(
    val currencies: List<CurrencyPresentation>,
    val showProgress: Boolean,
    val showError: Boolean
)