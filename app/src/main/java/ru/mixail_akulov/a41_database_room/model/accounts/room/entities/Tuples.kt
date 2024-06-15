package ru.mixail_akulov.a41_database_room.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class AccountSignInTuple(
    val id: Long,
    val password: String
)

data class AccountUpdateUsernameTuple(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    val username: String
)