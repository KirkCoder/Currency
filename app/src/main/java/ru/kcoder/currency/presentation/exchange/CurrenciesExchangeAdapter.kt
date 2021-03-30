package ru.kcoder.currency.presentation.exchange

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter

class CurrenciesExchangeAdapter : AsyncListDifferDelegationAdapter<CurrencyAmountPresentation>(
    CurrencyAmountPresentationDiffUtilCallback
) {

    init {
        delegatesManager.addDelegate(
            CurrencyAmountDelegate()
        )
    }
}