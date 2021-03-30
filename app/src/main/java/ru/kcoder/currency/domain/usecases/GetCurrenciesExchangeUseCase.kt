package ru.kcoder.currency.domain.usecases

import io.reactivex.Single
import ru.kcoder.currency.config.DefaultCurrencyProvider
import ru.kcoder.currency.data.error.CantCalculateSelectedCurrency
import ru.kcoder.currency.domain.model.CurrencyAmount
import ru.kcoder.currency.domain.model.CurrencyExchange
import ru.kcoder.currency.domain.repository.CurrenciesExchangeRepository
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetCurrenciesExchangeUseCase @Inject constructor(
    private val currenciesExchangeRepository: CurrenciesExchangeRepository,
    private val defaultCurrencyProvider: DefaultCurrencyProvider,
    private val currencyAmountMapper: CurrencyAmount.Mapper,
) {

    fun execute(selectedCurrency: CurrencyAmount): Single<List<CurrencyAmount>> {
        return currenciesExchangeRepository.getCurrenciesExchange()
            .map { currencyExchange ->
                val defaultCurrency = defaultCurrencyProvider.defaultCurrencyName
                val amountInDefaultCurrency =
                    getAmountInDefaultCurrency(selectedCurrency, defaultCurrency, currencyExchange)
                currencyAmountMapper.map(currencyExchange, amountInDefaultCurrency)
            }
    }

    private fun getAmountInDefaultCurrency(
        selectedCurrency: CurrencyAmount,
        defaultCurrency: String,
        currencyExchange: CurrencyExchange
    ): BigDecimal {
        return if (selectedCurrency.name == defaultCurrency) {
            selectedCurrency.amount
        } else {
            val concatCurrencyName = "${defaultCurrency}${selectedCurrency.name}"
            val defaultCurrencyRate =
                currencyExchange.currencyNameWithAmount[concatCurrencyName]?.toBigDecimalOrNull()
                    ?: throw CantCalculateSelectedCurrency()
            selectedCurrency.amount.divide(defaultCurrencyRate, SCALE_MODE, RoundingMode.HALF_UP)
        }
    }

    companion object {
        private const val SCALE_MODE = 2
    }
}