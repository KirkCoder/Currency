package ru.kcoder.currency.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.kcoder.currency.config.DefaultCurrencyProvider
import ru.kcoder.currency.data.storage.KeyValueStorage
import ru.kcoder.currency.domain.repository.SelectCurrencyRepository
import javax.inject.Inject

class SelectCurrencyRepositoryImpl @Inject constructor(
    private val keyValueStorage: KeyValueStorage,
    private val defaultCurrencyProvider: DefaultCurrencyProvider,
) : SelectCurrencyRepository {

    override fun selectCurrency(name: String): Completable {
        return keyValueStorage.saveString(SelectCurrencyRepository.SELECTED_CURRENCY_KEY, name)
    }

    override fun getSelectedCurrency(): Single<String> {
        return observeSelectedCurrency().firstOrError()
    }

    override fun observeSelectedCurrency(): Observable<String> {
        return keyValueStorage.observeForKeyWithDefaultValue(
            preferenceKey = SelectCurrencyRepository.SELECTED_CURRENCY_KEY,
            defaultValue = defaultCurrencyProvider.defaultCurrencyName
        )
    }
}