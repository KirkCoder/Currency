package ru.kcoder.currency.domain.model

import ru.kcoder.currency.config.DefaultCurrencyProvider
import java.math.BigDecimal
import javax.inject.Inject

data class CurrencyAmount(
    val name: String,
    val amount: BigDecimal
) {

    class Mapper @Inject constructor(
        private val defaultCurrencyProvider: DefaultCurrencyProvider
    ) {

        fun map(
            currencyExchange: CurrencyExchange,
            amountInDefaultCurrency: BigDecimal
        ): List<CurrencyAmount> {
            val defaultCurrencyLength = defaultCurrencyProvider.defaultCurrencyName.length
            return currencyExchange.currencyNameWithAmount.mapNotNull { nameWithAmount ->
                val amount = nameWithAmount.value.toBigDecimalOrNull() ?: return@mapNotNull null
                CurrencyAmount(
                    name = getCurrencyName(nameWithAmount, defaultCurrencyLength),
                    amount = amount.multiply(amountInDefaultCurrency)
                )
            }
        }

        private fun getCurrencyName(
            nameWithAmount: Map.Entry<String, String>,
            defaultCurrencyLength: Int
        ): String {
            return nameWithAmount.key.substring(
                defaultCurrencyLength,
                nameWithAmount.key.length
            )
        }
    }
}