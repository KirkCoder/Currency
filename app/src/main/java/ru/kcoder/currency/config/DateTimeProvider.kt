package ru.kcoder.currency.config

import java.util.*

interface DateTimeProvider {
    val currentDateTime: Date
    val cacheUpdateTime: Long
}