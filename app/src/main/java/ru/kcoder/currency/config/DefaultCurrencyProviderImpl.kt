package ru.kcoder.currency.config

import javax.inject.Inject

class DefaultCurrencyProviderImpl @Inject constructor() : DefaultCurrencyProvider {
    override val defaultCurrencyName: String = "USD"
}