package ru.kcoder.currency.presentation.exchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.currency_amount_item.view.*
import ru.kcoder.currency.R

class CurrencyAmountDelegate :
    AbsListItemAdapterDelegate<CurrencyAmountPresentation, CurrencyAmountPresentation, CurrencyAmountDelegate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.currency_amount_item, parent, false)
        )
    }

    override fun isForViewType(
        item: CurrencyAmountPresentation,
        items: MutableList<CurrencyAmountPresentation>,
        position: Int
    ): Boolean {
        return true
    }

    override fun onBindViewHolder(
        item: CurrencyAmountPresentation,
        holder: ViewHolder,
        payloads: MutableList<Any>
    ) {
        with(holder.itemView) {
            currencyNameTextView.text = item.name
            currencyAmountTextView.text = item.amount
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}