package ru.mixail_akulov.a41_database_room.screens.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mixail_akulov.a41_database_room.R
import ru.mixail_akulov.a41_database_room.model.EmptyFieldException
import ru.mixail_akulov.a41_database_room.model.StorageException
import ru.mixail_akulov.a41_database_room.model.accounts.AccountsRepository
import ru.mixail_akulov.a41_database_room.utils.MutableLiveEvent
import ru.mixail_akulov.a41_database_room.utils.MutableUnitLiveEvent
import ru.mixail_akulov.a41_database_room.utils.publishEvent
import ru.mixail_akulov.a41_database_room.utils.share

class EditProfileViewModel(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _initialUsernameEvent = MutableLiveEvent<String>()
    val initialUsernameEvent = _initialUsernameEvent.share()

    private val _saveInProgress = MutableLiveData(false)
    val saveInProgress = _saveInProgress.share()

    private val _goBackEvent = MutableUnitLiveEvent()
    val goBackEvent = _goBackEvent.share()

    private val _showErrorEvent = MutableLiveEvent<Int>()
    val showErrorEvent = _showErrorEvent.share()

    init {
        viewModelScope.launch {
            val account = accountsRepository.getAccount()
                .filterNotNull()
                .first()
            _initialUsernameEvent.publishEvent(account.username)
        }
    }

    fun saveUsername(newUsername: String) {
        viewModelScope.launch {
            showProgress()
            try {
                accountsRepository.updateAccountUsername(newUsername)
                goBack()
            } catch (e: EmptyFieldException) {
                showEmptyFieldErrorMessage()
            } catch (e: StorageException) {
                showStorageErrorMessage()
            } finally {
                hideProgress()
            }
        }
    }

    private fun goBack() = _goBackEvent.publishEvent()

    private fun showProgress() {
        _saveInProgress.value = true
    }

    private fun hideProgress() {
        _saveInProgress.value = false
    }

    private fun showEmptyFieldErrorMessage() = _showErrorEvent.publishEvent(R.string.field_is_empty)

    private fun showStorageErrorMessage() = _showErrorEvent.publishEvent(R.string.storage_error)

}