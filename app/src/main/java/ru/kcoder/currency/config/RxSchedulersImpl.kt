package ru.kcoder.currency.config

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RxSchedulersImpl @Inject constructor() : RxSchedulers {

    override val io: Scheduler = Schedulers.io()

    override val computation: Scheduler = Schedulers.computation()

    override val mainThread: Scheduler = AndroidSchedulers.mainThread()

    override val trampoline: Scheduler = Schedulers.trampoline()

}