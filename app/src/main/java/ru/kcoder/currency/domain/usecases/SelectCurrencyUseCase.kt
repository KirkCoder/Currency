package ru.kcoder.currency.domain.usecases

import io.reactivex.Completable
import ru.kcoder.currency.domain.repository.SelectCurrencyRepository
import javax.inject.Inject

class SelectCurrencyUseCase @Inject constructor(
    private val selectCurrencyRepository: SelectCurrencyRepository
) {
    fun execute(name: String): Completable {
        return selectCurrencyRepository.selectCurrency(name)
    }
}