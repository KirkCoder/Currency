package ru.kcoder.currency.data.error

data class CantLoadCurrencyException(
    val success: Boolean?,
    val code: Int?,
    val info: String?
) : Throwable()