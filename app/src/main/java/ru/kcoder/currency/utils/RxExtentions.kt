package ru.kcoder.currency.utils

import io.reactivex.Single
import io.reactivex.rxkotlin.Singles

fun <T : Any, U : Any> Single<T>.zipToPair(source: Single<U>): Single<Pair<T, U>> =
    Singles.zip(this, source) { first, second -> first to second }