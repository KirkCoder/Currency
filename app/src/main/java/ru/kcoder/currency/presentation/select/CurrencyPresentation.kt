package ru.kcoder.currency.presentation.select

import ru.kcoder.currency.domain.model.Currency
import javax.inject.Inject

data class CurrencyPresentation(
    val name: String,
    val description: String
) {

    class Formatter @Inject constructor() {

        fun format(currencies: List<Currency>): List<CurrencyPresentation> {
            return currencies.map { currency ->
                CurrencyPresentation(
                    name = currency.name,
                    description = currency.description
                )
            }
        }
    }

}