package com.plcoding.cryptotracker.crypto.presentation.coin_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.cryptotracker.core.domain.util.onError
import com.plcoding.cryptotracker.core.domain.util.onSuccess
import com.plcoding.cryptotracker.crypto.domain.CoinDataSource
import com.plcoding.cryptotracker.crypto.presentation.models.toCoinUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * we passed the definition of CoinDataSource in our viewModel because when we want to
 * test our viewModel it is possible to pass our fake implementation of CoinDataSource.
 * -----
 * we use we model to keep our ui state and update ui. For ex, when you rotate device,
 * our MainActivity will be finished and recreated again. In this case our viewModel can
 * store our data because it is independent from activity lifecycle.
 * -----
 * in viewModel keeping stateFlow is better experience then keeping composeState, because,
 * if you need to use flow operators it is possible to use them in viewModel with stateFlow. If you keep compose
 * state here you can not use flow operators. Keeping state flow in viewModel and converting it compose state in screen
 * better experience.
 * ------
 * fetching initial data in init function can create some sideEffects, for ex, when you create viewModel instance this means
 * fetching coins from remote API. while testing it can have some problems. In this case fetching data when UI observes our state is
 * a better experience.
 */

class CoinListViewModel(
    private val coinDataSource: CoinDataSource
) : ViewModel() {

    /**
     * here we created _state as private variable and exposed state as public variable.
     * the main purpose of this, when there is a bug in viewModel, we are totally sure that
     * we only update, _state from our viewModel and observe state from UI.
     */
    private val _state = MutableStateFlow(CoinListState())
    val state = _state.onStart {
        /** fetch coins when UI observes our state */
        getCoins()
    }.stateIn(
        /** fetch data if collector disappears more then 5 seconds, for example don't fetch data when rotate device */
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = CoinListState()
    )

    /**
     * Channel is very similar with sharedFlow, this means for example an error happened this event send only one time
     * they are not cached. If a new subscriber appears for example after rotation change, this events don't send to UI layer
     * which we want here.
     */
    private val _events = Channel<CoinListEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: CoinListAction) {
        when (action) {
            is CoinListAction.OnCoinClick -> {
                _state.update {
                    it.copy(
                        selectedCoin = action.coin
                    )
                }
            }
        }
    }

    private fun getCoins() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            coinDataSource.getCoins()
                .onSuccess { coins ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            coins = coins.map { coin -> coin.toCoinUi() }
                        )
                    }
                }
                .onError { error ->
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _events.send(CoinListEvent.Error(error))
                }
        }
    }

}