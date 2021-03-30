package ru.kcoder.currency.data.dto

import com.google.gson.annotations.SerializedName

data class CurrenciesDto(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("error") val error: ErrorDto?,
    @SerializedName("terms") val terms: String?,
    @SerializedName("source") val source: String?,
    @SerializedName("privacy") val privacy: String?,
    @SerializedName("timestamp") val timestamp: String?,
    @SerializedName("currencies") val currencies: Map<String, String>?,
    @SerializedName("quotes") val quotes: Map<String, String>?,
)