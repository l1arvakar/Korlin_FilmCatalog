package by.bsuir.filmcatalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.filmcatalog.data.UserData
import by.bsuir.filmcatalog.services.AuthenticationService
import by.bsuir.filmcatalog.services.DatabaseService
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    private val _user = AuthenticationService().getSignedInUser()
    private val _dbUid = DatabaseService(_user?.uid)

    private val _isSignedIn = MutableStateFlow(_user != null)
    val isSignedIn: StateFlow<Boolean> = _isSignedIn

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    init {
        viewModelScope.launch {
            _dbUid.getUserDataRealtime().collect { userData ->
                _userData.value = userData
            }
        }

        viewModelScope.launch {
            AuthenticationService().getIsSingedInRealtime().collect{
                _isSignedIn.value = it
            }
        }
    }

    fun updateUserData(
        firstname: String,
        lastname: String,
        description: String,
        birthday: Timestamp,
        gender: String,
        address: String,
        filmCountry: String,
        phone: String,
        favActor: String,
        favDirector: String
    ) {
        viewModelScope.launch {
            _dbUid.updateUserData(
                firstname,
                lastname,
                description,
                birthday,
                gender,
                address,
                filmCountry,
                phone,
                favActor,
                favDirector
            )
        }
    }

    fun deleteAccount(onSuccess: () -> Unit) {
        viewModelScope.launch { AuthenticationService().deleteAccount(onSuccess) }
    }
}