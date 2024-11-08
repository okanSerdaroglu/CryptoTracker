package com.plcoding.cryptotracker.crypto.domain

import com.plcoding.cryptotracker.core.domain.util.NetworkError
import com.plcoding.cryptotracker.core.domain.util.Result

/**
 * this is definition of CoinDataSource, the implementation should be in data layer because domain does not know about data layer
 */

interface CoinDataSource {

    suspend fun getCoins(): Result<List<Coin>, NetworkError>

}