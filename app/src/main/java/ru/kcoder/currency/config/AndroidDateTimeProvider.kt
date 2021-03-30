package ru.kcoder.currency.config

import java.util.*
import javax.inject.Inject

class AndroidDateTimeProvider @Inject constructor() : DateTimeProvider {
    override val currentDateTime: Date get() = GregorianCalendar.getInstance().time

    override val cacheUpdateTime: Long = UPDATE_PERIOD

    companion object {
        private const val UPDATE_PERIOD = 1800000L
    }
}