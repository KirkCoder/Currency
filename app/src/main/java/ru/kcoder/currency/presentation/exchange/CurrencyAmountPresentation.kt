package ru.kcoder.currency.presentation.exchange

import ru.kcoder.currency.domain.model.CurrencyAmount
import java.math.RoundingMode
import javax.inject.Inject

data class CurrencyAmountPresentation(
    val name: String,
    val amount: String
) {

    class Formatter @Inject constructor() {
        fun format(currenciesAmount: List<CurrencyAmount>): List<CurrencyAmountPresentation> {
            return currenciesAmount.map { currencyAmount ->
                CurrencyAmountPresentation(
                    name = currencyAmount.name,
                    amount = currencyAmount.amount.setScale(2, RoundingMode.CEILING).toString()
                )
            }
        }
    }
}