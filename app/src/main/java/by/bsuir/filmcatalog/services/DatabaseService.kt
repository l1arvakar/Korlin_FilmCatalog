package by.bsuir.filmcatalog.services

import by.bsuir.filmcatalog.data.Film
import by.bsuir.filmcatalog.data.UserData
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DatabaseService(private val uid: String?) {
    private val db = Firebase.firestore
    private val userCollection = db.collection("users")
    private val carsCollection = db.collection("film")

    fun createUserData(): Task<Void> {
        val userData = hashMapOf(
            "firstname" to "",
            "lastname" to "",
            "description" to "",
            "birthday" to Timestamp.now(),
            "gender" to "Мужчина",
            "address" to "",
            "filmCountry" to "DE",
            "favActor" to "",
            "fav_Director" to "",
            "favouriteCars" to listOf<String>()
        )

        return uid?.let { userCollection.document(it).set(userData) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun updateUserData(
        firstname: String,
        lastname: String,
        descritipon: String,
        birthday: Timestamp,
        gender: String,
        address: String,
        filmCountry: String,
        favActor: String,
        favDirector: String,
        favDirector1: String
    ): Task<Void> {
        val userData = mapOf(
            "firstname" to firstname,
            "lastname" to lastname,
            "birthday" to birthday,
            "description" to descritipon,
            "gender" to gender,
            "address" to address,
            "filmCountry" to filmCountry,
            "favActor" to favActor,
            "favDirector" to favDirector
        )

        return uid?.let { userCollection.document(it).update(userData) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun updateUserFavCars(favCars: List<String>): Task<Void> {
        return uid?.let { userCollection.document(it).update("favouriteCars", favCars) }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun deleteUserData(): Task<Void> {
        return uid?.let { userCollection.document(it).delete() }
            ?: throw IllegalStateException("UID cannot be null")
    }

    fun getUserDataRealtime(): Flow<UserData?> = callbackFlow {
        val snapshotListener =
            uid?.let {
                userCollection.document(it).addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    val userData = snapshot?.toObject<UserData?>()
                    userData?.uid = it
                    val temp = snapshot?.get("favouriteCars") as List<*>?
                    if(temp != null){
                        userData?.favFilms = temp as List<String>
                    }
                    trySend(userData).isSuccess
                }
            }
        awaitClose {
            snapshotListener?.remove()
        }
    }

    fun getCarsRealtime(): Flow<List<Film>> = callbackFlow {
        val snapshotListener = carsCollection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                close(e)
                return@addSnapshotListener
            }
            val films = snapshot?.documents?.mapNotNull {
                val film = it.toObject<Film>()
                film?.uid = it.id
                film?.isFav = false
                film
            }.orEmpty()
            trySend(films).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }
}