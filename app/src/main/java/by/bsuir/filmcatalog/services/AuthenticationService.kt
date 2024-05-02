package by.bsuir.filmcatalog.services

import by.bsuir.filmcatalog.data.SignInResult
import by.bsuir.filmcatalog.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException


class AuthenticationService {
    private val auth = Firebase.auth

    suspend fun signInWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = user?.run {
                    User(uid)
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun registerWithEmailAndPassword(email: String, password: String): SignInResult {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.uid?.let { uid ->
                DatabaseService(uid = uid).createUserData().await()
            }
            SignInResult(
                data = user?.run {
                    User(uid)
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): User? = auth.currentUser?.run {
        User(
            uid = uid
        )
    }

    fun getIsSingedInRealtime(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener {
            it.currentUser != null
        }
        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun deleteAccount(onSuccess: () -> Unit) {
        val uid = auth.currentUser?.uid
        try {
            auth.currentUser?.delete()?.await()
        } catch (e: FirebaseAuthException) {
            println(e.toString())
            if (e.errorCode == "requires-recent-login") {
                println(e.toString())
            } else {
                println(e.toString())
            }
        } catch (e: Exception) {
            println(e.toString())
        }
        if (uid != null) {
            DatabaseService(uid = uid).deleteUserData().addOnSuccessListener { onSuccess() }
        }
    }
}