package ru.mixail_akulov.a41_database_room.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.mixail_akulov.a41_database_room.model.accounts.room.AccountsDao
import ru.mixail_akulov.a41_database_room.model.accounts.room.entities.AccountDbEntity
import ru.mixail_akulov.a41_database_room.model.boxes.room.BoxesDao
import ru.mixail_akulov.a41_database_room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ru.mixail_akulov.a41_database_room.model.boxes.room.entities.BoxDbEntity

@Database(
    version = 1,
    entities = [
        AccountDbEntity::class,
        BoxDbEntity::class,
        AccountBoxSettingDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao

}