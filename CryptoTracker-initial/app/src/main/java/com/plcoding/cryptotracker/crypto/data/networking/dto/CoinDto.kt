package com.plcoding.cryptotracker.crypto.data.networking.dto

import kotlinx.serialization.Serializable

/**
 * this annotation automatically parse json to this class. DTO class and UI models should be separate.
 * Because of two different reasons
 * 1 - sometimes domain model and response model can be different
 * 2 - sometimes response model can consist difficult data to serialize for example timeZones, dates, etc. This type of data is very easy to serialize
 * as string then map to domain model. Sometimes mapping a data can be easier than serializing it.
 */

@Serializable
data class CoinDto(
    val id: String,
    val rank: Int,
    val name: String,
    val symbol: String,
    val marketCapUsd: Double,
    val priceUsd: Double,
    val changePercent24Hr: Double,
)
