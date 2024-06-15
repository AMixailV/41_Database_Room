package ru.mixail_akulov.a41_database_room.model.accounts

import kotlinx.coroutines.flow.Flow
import ru.mixail_akulov.a41_database_room.model.*
import ru.mixail_akulov.a41_database_room.model.accounts.entities.Account
import ru.mixail_akulov.a41_database_room.model.accounts.entities.SignUpData

/**
 * Репозиторий с действиями, связанными с учетной записью,
 * например, вход, регистрация, редактирование учетной записи и т. д.
 */
interface AccountsRepository {

    /**
     * Whether user is signed-in or not.
     */
    suspend fun isSignedIn(): Boolean

    /**
     * Try to sign-in with the email and password.
     * @throws [EmptyFieldException]
     * @throws [AuthException]
     * @throws [StorageException]
     */
    suspend fun signIn(email: String, password: String)

    /**
     * Create a new account.
     * @throws [EmptyFieldException]
     * @throws [PasswordMismatchException]
     * @throws [AccountAlreadyExistsException]
     * @throws [StorageException]
     */
    suspend fun signUp(signUpData: SignUpData)

    /**
     * Sign-out from the app.
     */
    suspend fun logout()

    /**
     * Get the account info of the current signed-in user.
     */
    suspend fun getAccount(): Flow<Account?>

    /**
     * Change the username of the current signed-in user.
     * @throws [EmptyFieldException]
     * @throws [AuthException]
     * @throws [StorageException]
     */
    suspend fun updateAccountUsername(newUsername: String)

}