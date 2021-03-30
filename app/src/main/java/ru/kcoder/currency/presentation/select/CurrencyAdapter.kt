package ru.kcoder.currency.presentation.select

import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class CurrencyAdapter(
    onSelectCurrency: (CurrencyPresentation) -> Unit
) : ListDelegationAdapter<List<CurrencyPresentation>>() {

    init {
        delegatesManager.addDelegate(CurrencyDelegate(onSelectCurrency))
    }
}