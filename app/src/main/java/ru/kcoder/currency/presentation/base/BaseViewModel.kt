package ru.kcoder.currency.presentation.base

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.kcoder.currency.config.RxSchedulers
import timber.log.Timber

abstract class BaseViewModel(
    protected val rxSchedulers: RxSchedulers
) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()


    protected fun <T : Any> Observable<T>.schedule(
        onNext: ((T) -> Unit)? = null,
        onComplete: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler = rxSchedulers.io,
        observeOnScheduler: Scheduler = rxSchedulers.mainThread
    ) {
        this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribe(
                WrapperObserver(onNext, onComplete, onError, onSubscribe)
            )
    }

    protected fun <T : Any> Single<T>.schedule(
        onSuccess: ((T) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler = rxSchedulers.io,
        observeOnScheduler: Scheduler = rxSchedulers.mainThread
    ) {
        this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribe(
                SingleWrapperObserver(onSuccess, onError, onSubscribe)
            )
    }

    protected fun Completable.schedule(
        onComplete: (() -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onSubscribe: ((Disposable) -> Unit)? = null,
        subscribeOnScheduler: Scheduler = rxSchedulers.io,
        observeOnScheduler: Scheduler = rxSchedulers.mainThread
    ) {
        this.subscribeOn(subscribeOnScheduler)
            .observeOn(observeOnScheduler)
            .subscribe(
                CompletableWrapperObserver(onComplete, onError, onSubscribe)
            )
    }


    private inner class CompletableWrapperObserver(
        private val onComplete: (() -> Unit)?,
        private val onError: ((Throwable) -> Unit)?,
        private val onSubscribe: ((Disposable) -> Unit)?
    ) : CompletableObserver {

        override fun onSubscribe(disposable: Disposable) {
            if (!disposable.isDisposed) {
                compositeDisposable.add(disposable)
            }
            onSubscribe?.invoke(disposable)
        }

        override fun onComplete() {
            onComplete?.invoke()
        }

        override fun onError(e: Throwable) {
            onError?.invoke(e)
            Timber.e(e)
        }
    }

    private inner class WrapperObserver<T : Any>(
        private val onNext: ((T) -> Unit)?,
        private val onComplete: (() -> Unit)?,
        private val onError: ((Throwable) -> Unit)?,
        private val onSubscribe: ((Disposable) -> Unit)?
    ) : Observer<T> {

        override fun onSubscribe(disposable: Disposable) {
            if (!disposable.isDisposed) {
                compositeDisposable.add(disposable)
            }
            onSubscribe?.invoke(disposable)
        }

        override fun onComplete() {
            onComplete?.invoke()
        }

        override fun onNext(t: T) {
            onNext?.invoke(t)
        }

        override fun onError(e: Throwable) {
            onError?.invoke(e)
            Timber.e(e)
        }
    }

    private inner class SingleWrapperObserver<T : Any>(
        private val onSuccess: ((T) -> Unit)?,
        private val onError: ((Throwable) -> Unit)?,
        private val onSubscribe: ((Disposable) -> Unit)?
    ) : SingleObserver<T> {

        override fun onSubscribe(disposable: Disposable) {
            if (!disposable.isDisposed) {
                compositeDisposable.add(disposable)
            }
            onSubscribe?.invoke(disposable)
        }

        override fun onSuccess(t: T) {
            onSuccess?.invoke(t)
        }

        override fun onError(e: Throwable) {
            onError?.invoke(e)
            Timber.e(e)
        }
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}