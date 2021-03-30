package ru.kcoder.currency.presentation.exchange

import ru.kcoder.currency.R
import ru.kcoder.currency.presentation.base.ResourceDataStore
import javax.inject.Inject

data class SelectedCurrencyPresentation(
    val fullName: String,
    val shortName: String,
) {

    class Formatter @Inject constructor(
        private val resourceDataStore: ResourceDataStore,
    ) {

        fun format(selectedCurrency: String): SelectedCurrencyPresentation {
            return SelectedCurrencyPresentation(
                fullName = resourceDataStore.getString(
                    R.string.selected_currency,
                    selectedCurrency
                ),
                shortName = selectedCurrency,
            )
        }
    }
}