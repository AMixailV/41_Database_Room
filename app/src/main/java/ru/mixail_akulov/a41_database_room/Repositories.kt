package ru.mixail_akulov.a41_database_room

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.mixail_akulov.a41_database_room.model.accounts.AccountsRepository
import ru.mixail_akulov.a41_database_room.model.accounts.room.RoomAccountsRepository
import ru.mixail_akulov.a41_database_room.model.boxes.BoxesRepository
import ru.mixail_akulov.a41_database_room.model.boxes.room.RoomBoxesRepository
import ru.mixail_akulov.a41_database_room.model.room.AppDatabase
import ru.mixail_akulov.a41_database_room.model.settings.AppSettings
import ru.mixail_akulov.a41_database_room.model.settings.SharedPreferencesAppSettings

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("initial_database.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(database.getAccountsDao(), appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        RoomBoxesRepository(accountsRepository, database.getBoxesDao(), ioDispatcher)
    }

    /**
     * Вызовите этот метод во всех компонентах приложения,
     * которые могут быть созданы при восстановлении запуска приложения (например, в onCreate действий и служб).
     */
    fun init(context: Context) {
        applicationContext = context
    }
}