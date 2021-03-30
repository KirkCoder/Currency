package ru.kcoder.currency.presentation.base

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import kotlinx.android.synthetic.main.content_main.*
import ru.kcoder.currency.App
import ru.kcoder.currency.R

abstract class BaseFragment : Fragment() {

    protected val addCompatActivity: AppCompatActivity?
        get() {
            return (activity as? AppCompatActivity)
        }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDI()
    }

    @CallSuper
    open fun initDI() {
        App.instance.appComponent.inject(this)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkHomeButton()
    }

    private fun checkHomeButton() {
        if (findNavController().previousBackStackEntry != null) {
            addCompatActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            addCompatActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    fun <T> observe(liveData: LiveData<T>, observer: (data: T) -> Unit) {
        liveData.observe(viewLifecycleOwner, Observer(observer::invoke))
    }
}