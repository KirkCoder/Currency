package ru.kcoder.currency.presentation.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_currency_exchange.*
import kotlinx.android.synthetic.main.fragment_currency_exchange.errorLayout
import kotlinx.android.synthetic.main.fragment_currency_exchange.progressBar
import ru.kcoder.currency.App
import ru.kcoder.currency.R
import ru.kcoder.currency.presentation.base.BaseFragment
import ru.kcoder.currency.presentation.base.ViewModelFactory
import ru.kcoder.currency.presentation.base.initViewModel
import ru.kcoder.currency.presentation.select.CurrenciesItemDecorator
import javax.inject.Inject

class CurrencyExchangeFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: CurrencyExchangeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel<CurrencyExchangeViewModel>(viewModelFactory)
    }

    private val currenciesExchangeAdapter = CurrenciesExchangeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_currency_exchange, container, false)
    }

    override fun initDI() {
        super.initDI()
        App.instance.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        subscribeUi()
        initListeners()
    }

    private fun initRecycler() {
        with(currenciesAmountRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = currenciesExchangeAdapter
            addItemDecoration(
                CurrenciesItemDecorator(
                    marginBottom = context.resources.getDimension(R.dimen.side_margin).toInt()
                )
            )
        }
    }

    private fun initListeners() {
        selectedCurrencyButton.setOnClickListener {
            findNavController().navigate(R.id.action_CurrencyExchangeFragment_to_SelectCurrencyFragment)
        }

        amountInputEditText.addTextChangedListener(onTextChanged = { text, _, _, _ ->
            text?.let {
                viewModel.calculateChanges(text.toString())
            }
        })

        retryButton.setOnClickListener {
            calculateExistingExchange()
        }

        closeButton.setOnClickListener {
            viewModel.close()
        }
    }

    private fun subscribeUi() {
        observe(viewModel.selectedCurrency, ::setSelectedCurrency)
        observe(viewModel.currencyExchangeLiveData, ::handleCurrencyExchange)
        observe(viewModel.closeViewModel, ::close)
    }

    private fun setSelectedCurrency(currency: SelectedCurrencyPresentation) {
        selectedCurrencyButton.text = currency.fullName
    }

    private fun calculateExistingExchange() {
        viewModel.calculateChanges(amountInputEditText.text?.toString() ?: "")
    }

    private fun handleCurrencyExchange(
        currenciesExchangePresentation: CurrenciesExchangePresentation
    ) {
        progressBar.isVisible = currenciesExchangePresentation.showProgress
        errorLayout.isVisible = currenciesExchangePresentation.showError
        if (!currenciesExchangePresentation.showProgress) {
            currenciesExchangeAdapter.items = currenciesExchangePresentation.currencies
        }
    }

    private fun close(any: Any) {
        addCompatActivity?.onBackPressed()
    }
}
