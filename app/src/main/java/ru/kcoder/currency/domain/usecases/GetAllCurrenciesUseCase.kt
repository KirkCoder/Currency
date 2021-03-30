package ru.kcoder.currency.domain.usecases

import io.reactivex.Single
import ru.kcoder.currency.domain.model.Currency
import ru.kcoder.currency.domain.repository.AllCurrenciesRepository
import javax.inject.Inject

class GetAllCurrenciesUseCase @Inject constructor(
    private val allCurrenciesRepository: AllCurrenciesRepository
) {

    fun execute(): Single<List<Currency>> {
        return allCurrenciesRepository.getAllCurrencies()
    }

}