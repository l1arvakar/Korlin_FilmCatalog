package by.bsuir.filmcatalog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.filmcatalog.data.Film
import by.bsuir.filmcatalog.data.UserData
import by.bsuir.filmcatalog.services.AuthenticationService
import by.bsuir.filmcatalog.services.DatabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmListViewModel : ViewModel() {
    private val _db = DatabaseService(null)
    private val _user = AuthenticationService().getSignedInUser()
    private val _dbUid = DatabaseService(_user?.uid)

    private val _films = MutableStateFlow<List<Film>>(emptyList())
    val films: StateFlow<List<Film>> = _films

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> = _userData

    init {
        viewModelScope.launch {
            _db.getCarsRealtime().collect { listOfCars ->
                _films.value = listOfCars
            }
        }
        viewModelScope.launch {
            _dbUid.getUserDataRealtime().collect { userData ->
                _userData.value = userData
            }
        }
    }
}