package ru.kcoder.currency.domain.usecases

import io.reactivex.Observable
import ru.kcoder.currency.domain.repository.SelectCurrencyRepository
import javax.inject.Inject

class ObserveSelectedCurrencyUseCase @Inject constructor(
    private val selectCurrencyRepository: SelectCurrencyRepository
){
    fun execute(): Observable<String> {
        return selectCurrencyRepository.observeSelectedCurrency()
    }
}