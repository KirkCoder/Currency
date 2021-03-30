package ru.kcoder.currency.presentation.exchange

import androidx.recyclerview.widget.DiffUtil

object CurrencyAmountPresentationDiffUtilCallback :
    DiffUtil.ItemCallback<CurrencyAmountPresentation>() {

    override fun areItemsTheSame(
        oldItem: CurrencyAmountPresentation,
        newItem: CurrencyAmountPresentation
    ): Boolean {
        return newItem.javaClass == oldItem.javaClass
    }

    override fun areContentsTheSame(
        oldItem: CurrencyAmountPresentation,
        newItem: CurrencyAmountPresentation
    ): Boolean {
        return oldItem == newItem
    }

}