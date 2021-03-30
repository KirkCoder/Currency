package ru.kcoder.currency.di

import dagger.Component
import ru.kcoder.currency.App
import ru.kcoder.currency.presentation.exchange.CurrencyExchangeFragment
import ru.kcoder.currency.presentation.base.BaseFragment
import ru.kcoder.currency.presentation.select.SelectCurrencyFragment
import javax.inject.Singleton

@Component(
    modules = [
        AndroidModule::class,
        ApiModule::class,
        DataBindings::class,
        GsonModule::class,
    ]
)
@Singleton
interface AppComponent {

    fun inject(app: App)

    fun inject(currencyExchangeFragment: CurrencyExchangeFragment)

    fun inject(baseFragment: BaseFragment)

    fun inject(selectCurrencyFragment: SelectCurrencyFragment)
}