package ru.mixail_akulov.a41_database_room.model.boxes.room

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import ru.mixail_akulov.a41_database_room.model.AuthException
import ru.mixail_akulov.a41_database_room.model.accounts.AccountsRepository
import ru.mixail_akulov.a41_database_room.model.boxes.BoxesRepository
import ru.mixail_akulov.a41_database_room.model.boxes.entities.Box
import ru.mixail_akulov.a41_database_room.model.boxes.entities.BoxAndSettings
import ru.mixail_akulov.a41_database_room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ru.mixail_akulov.a41_database_room.model.room.wrapSQLiteException

class RoomBoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesDao: BoxesDao,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    override suspend fun getBoxesAndSettings(onlyActive: Boolean): Flow<List<BoxAndSettings>> {
        return accountsRepository.getAccount()
            .flatMapLatest { account ->
                if (account == null) return@flatMapLatest flowOf(emptyList())
                queryBoxesAndSettings(account.id)
            }
            .mapLatest { boxAndSettings ->
                if (onlyActive) {
                    boxAndSettings.filter { it.isActive }
                } else {
                    boxAndSettings
                }
            }
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private fun queryBoxesAndSettings(accountId: Long): Flow<List<BoxAndSettings>> {
        return boxesDao.getBoxesAndSettings(accountId)
            .map { entities ->
                entities.map {
                    val boxEntity = it.key
                    val settingsEntity = it.value
                    BoxAndSettings(boxEntity.toBox(), settingsEntity == null || settingsEntity.isActive)
                }
            }
    }

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        val account = accountsRepository.getAccount().first() ?: throw AuthException()
        boxesDao.setActiveFlagForBox(
            AccountBoxSettingDbEntity(
                accountId = account.id,
                boxId = box.id,
                isActive = isActive
            )
        )
    }
}