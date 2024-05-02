package by.bsuir.filmcatalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await

class PreviewImageViewModel : ViewModel() {
    fun getFileUrl(path: String) = liveData {
        try {
            val storageReference = Firebase.storage.reference.child(path)
            val url = storageReference.downloadUrl.await()
            emit(url.toString())
        } catch (e: Exception) {
            emit(null)
        }
    }
}