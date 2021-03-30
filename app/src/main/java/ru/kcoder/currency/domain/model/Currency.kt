package ru.kcoder.currency.domain.model

import dagger.Reusable
import javax.inject.Inject

data class Currency(
    val name: String,
    val description: String
) {

    @Reusable
    class Mapper @Inject constructor() {

        fun map(currencies: Map<String, String>): List<Currency> {
            return currencies.map { entry ->
                Currency(entry.key, entry.value)
            }
        }
    }
}