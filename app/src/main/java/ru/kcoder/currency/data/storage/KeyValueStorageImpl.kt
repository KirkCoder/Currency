package ru.kcoder.currency.data.storage

import android.content.Context
import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class KeyValueStorageImpl @Inject constructor(
    context: Context
) : KeyValueStorage {

    private val prefs: SharedPreferences
    private val preferenceObserver = PublishSubject.create<String>()
    private val changeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            preferenceObserver.onNext(key)
        }

    init {
        prefs = context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
        prefs.registerOnSharedPreferenceChangeListener(changeListener)
    }

    override fun observeForKeyWithDefaultValue(
        preferenceKey: String,
        defaultValue: String
    ): Observable<String> {
        return Observable.create<String> { emitter ->

            val sharedPreferencesListener =
                SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                    if (key == preferenceKey) {
                        emitter.onNext(
                            sharedPreferences.getString(preferenceKey, defaultValue) ?: defaultValue
                        )
                    }
                }

            emitter.setCancellable {
                prefs.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
            }

            emitter.onNext(prefs.getString(preferenceKey, defaultValue) ?: defaultValue)
            prefs.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
        }
    }

    override fun saveString(key: String, value: String?): Completable {
        return Completable.fromAction {
            with(prefs.edit()) {
                putString(key, value)
                apply()
            }
        }
    }

    override fun saveLong(key: String, value: Long): Completable {
        return Completable.fromAction {
            with(prefs.edit()) {
                putLong(key, value)
                apply()
            }
        }
    }

    override fun getLong(key: String, default: Long): Single<Long> {
        return Single.fromCallable {
            prefs.getLong(key, default)
        }
    }

    companion object {
        const val STORAGE_NAME = "storage"
    }
}