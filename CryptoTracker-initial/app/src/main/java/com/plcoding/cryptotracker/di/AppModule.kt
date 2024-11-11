package com.plcoding.cryptotracker.di

import com.plcoding.cryptotracker.core.data.networking.HttpClientFactory
import com.plcoding.cryptotracker.crypto.data.networking.RemoteCoinDataSource
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.coin_list.CoinListViewModel
import io.ktor.client.engine.cio.*
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    /**
     * single -> singleton, we pass here CIO engine as Engine and while we are testing our project we can use MockEngine
     */
    single { HttpClientFactory.create(CIO.create()) }
    singleOf(::RemoteCoinDataSource).bind<CoinDataSource>()
    /** the same with  single { RemoteCoinDataSource(get()) }  :: RemoteCoinDataSource is abstraction that's why we use bind
     * this means that when you see RemoteCoinDataSource abstraction you should use CoinDataSource implementation */
    viewModelOf(::CoinListViewModel)
}