package ru.mixail_akulov.a41_database_room.screens.main.tabs.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mixail_akulov.a41_database_room.model.boxes.BoxesRepository
import ru.mixail_akulov.a41_database_room.utils.MutableLiveEvent
import ru.mixail_akulov.a41_database_room.utils.publishEvent
import ru.mixail_akulov.a41_database_room.utils.share

class BoxViewModel(
    private val boxId: Long,
    private val boxesRepository: BoxesRepository
) : ViewModel() {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(onlyActive = true)
                .map { boxes -> boxes.firstOrNull { it.box.id == boxId } }
                .collect { currentBox ->
                    _shouldExitEvent.publishEvent(currentBox == null)
                }
        }
    }
}