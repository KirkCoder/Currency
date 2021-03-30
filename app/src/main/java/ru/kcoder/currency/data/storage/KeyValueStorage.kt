package ru.kcoder.currency.data.storage

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface KeyValueStorage {
    fun saveString(key: String, value: String?): Completable

    fun saveLong(key: String, value: Long): Completable

    fun getLong(key: String, default: Long): Single<Long>

    fun observeForKeyWithDefaultValue(preferenceKey: String, defaultValue: String): Observable<String>
}