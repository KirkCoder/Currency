package ru.kcoder.currency.presentation.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.fragment_selected_currency.*
import ru.kcoder.currency.App
import ru.kcoder.currency.R
import ru.kcoder.currency.presentation.base.BaseFragment
import ru.kcoder.currency.presentation.base.ViewModelFactory
import ru.kcoder.currency.presentation.base.initViewModel
import javax.inject.Inject

class SelectCurrencyFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: SelectCurrencyViewModel by lazy(LazyThreadSafetyMode.NONE) {
        initViewModel<SelectCurrencyViewModel>(viewModelFactory)
    }

    private val currencyAdapter = CurrencyAdapter(::onSelectCurrency)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_selected_currency, container, false)
    }

    override fun initDI() {
        super.initDI()
        App.instance.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initListeners()
        subscribeUi()
    }

    private fun initRecycler() {
        with(allCurrenciesRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = currencyAdapter
            addItemDecoration(
                CurrenciesItemDecorator(
                    marginBottom = context.resources.getDimension(R.dimen.side_margin).toInt()
                )
            )
        }
    }

    private fun initListeners() {
        retryButton.setOnClickListener {
            viewModel.loadAllCurrencies()
        }

        closeButton.setOnClickListener {
            viewModel.close()
        }
    }

    private fun onSelectCurrency(currency: CurrencyPresentation) {
        viewModel.selectCurrency(currency)
    }

    private fun subscribeUi() {
        observe(viewModel.allCurrenciesLiveData, ::handleCurrencies)
        observe(viewModel.closeLiveData, ::close)
    }

    private fun handleCurrencies(allCurrenciesPresentation: AllCurrenciesPresentation) {
        errorLayout.isVisible = allCurrenciesPresentation.showError
        progressBar.isVisible = allCurrenciesPresentation.showProgress
        if (!allCurrenciesPresentation.showProgress) {
            allCurrenciesRecyclerView.isVisible = true
            currencyAdapter.items = allCurrenciesPresentation.currencies
            currencyAdapter.notifyDataSetChanged()
        }
    }

    private fun close(any: Any) {
        findNavController().popBackStack()
    }
}