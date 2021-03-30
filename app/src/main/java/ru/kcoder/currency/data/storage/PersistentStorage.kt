package ru.kcoder.currency.data.storage

import io.reactivex.Completable
import io.reactivex.Single

interface PersistentStorage {

    fun <T> writeToStorage(name: String, data: T): Completable

    fun <T> readFromStorage(name: String, clazz: Class<T>): Single<T>
}