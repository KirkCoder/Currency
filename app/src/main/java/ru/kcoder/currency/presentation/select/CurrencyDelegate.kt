package ru.kcoder.currency.presentation.select

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.currency_item.view.*
import ru.kcoder.currency.R

class CurrencyDelegate(
    private val onSelectCurrency: (CurrencyPresentation) -> Unit
) : AdapterDelegate<List<CurrencyPresentation>>() {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.currency_item, parent, false)
        )
    }

    override fun isForViewType(items: List<CurrencyPresentation>, position: Int): Boolean {
        return true
    }

    override fun onBindViewHolder(
        items: List<CurrencyPresentation>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val item = items[position]
        with(holder.itemView) {
            currencyNameTextView.text = item.name
            currencyDescriptionTextView.text = item.description
            setOnClickListener {
                onSelectCurrency(item)
            }
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer
}