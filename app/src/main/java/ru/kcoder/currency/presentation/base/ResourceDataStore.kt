package ru.kcoder.currency.presentation.base

import android.content.Context
import androidx.annotation.StringRes
import dagger.Reusable
import javax.inject.Inject

@Reusable
class ResourceDataStore @Inject constructor(
    private val context: Context
){
    fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg any: Any): String {
        return context.getString(resId, *any)
    }
}