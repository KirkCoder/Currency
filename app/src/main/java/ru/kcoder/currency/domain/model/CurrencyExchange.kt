package ru.kcoder.currency.domain.model

import javax.inject.Inject

data class CurrencyExchange(
    val currencyNameWithAmount: Map<String, String>
) {

    fun isEmpty() = currencyNameWithAmount.isEmpty()

    class Mapper @Inject constructor() {
        fun map(exchange: Map<String, String>): CurrencyExchange {
            return CurrencyExchange(
                currencyNameWithAmount = exchange
            )
        }
    }

}