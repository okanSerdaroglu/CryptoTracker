package com.plcoding.cryptotracker.crypto.domain

import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result
import com.plcoding.cryptotracker.crypto.data.networking.dto.CoinHistoryDto
import java.time.ZonedDateTime

/**
 * this is definition of CoinDataSource, the implementation should be in data layer because domain does not know about data layer
 */

interface CoinDataSource {

    suspend fun getCoins(): Result<List<Coin>, NetworkError>

    suspend fun getCoinHistory(
        coinId: String,
        start: ZonedDateTime,
        end: ZonedDateTime
    ): Result<List<CoinPrice>, NetworkError>

}