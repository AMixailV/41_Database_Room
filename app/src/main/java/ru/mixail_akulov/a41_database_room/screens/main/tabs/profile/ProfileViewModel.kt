package ru.mixail_akulov.a41_database_room.screens.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mixail_akulov.a41_database_room.model.accounts.AccountsRepository
import ru.mixail_akulov.a41_database_room.model.accounts.entities.Account
import ru.mixail_akulov.a41_database_room.utils.MutableLiveEvent
import ru.mixail_akulov.a41_database_room.utils.publishEvent
import ru.mixail_akulov.a41_database_room.utils.share

class ProfileViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _account = MutableLiveData<Account>()
    val account = _account.share()

    private val _restartFromLoginEvent = MutableLiveEvent<Unit>()
    val restartWithSignInEvent = _restartFromLoginEvent.share()

    init {
        viewModelScope.launch {
            accountsRepository.getAccount().collect {
                _account.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            accountsRepository.logout()
            restartAppFromLoginScreen()
        }
    }

    private fun restartAppFromLoginScreen() {
        _restartFromLoginEvent.publishEvent()
    }

}