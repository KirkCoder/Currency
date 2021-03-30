package ru.kcoder.currency.data.error

class CantLoadCurrencyExchangeException(
    val success: Boolean?,
    val code: Int?,
    val info: String?
): Throwable()