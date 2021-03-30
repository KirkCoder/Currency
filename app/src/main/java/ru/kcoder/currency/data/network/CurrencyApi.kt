package ru.kcoder.currency.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kcoder.currency.data.dto.CurrenciesDto

interface CurrencyApi {

    @GET("/list")
    fun getAllSupportedCurrencies(
        @Query("access_key") accessKey: String
    ): Single<CurrenciesDto>

    @GET("/live")
    fun getCurrencyExchange(
        @Query("access_key") accessKey: String, @Query("source") source: String
    ): Single<CurrenciesDto>
}