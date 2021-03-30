package ru.kcoder.currency.data.error

class CantLoadCurrencyExchangeException( // todo should use properties from this class in health analytics
    val success: Boolean?,
    val code: Int?,
    val info: String?
): Throwable()