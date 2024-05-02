package by.bsuir.filmcatalog.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.bsuir.filmcatalog.data.Film
import by.bsuir.filmcatalog.viewmodel.FilmListViewModel

@Composable
fun FilmList(onlyFavourites: Boolean, viewModel: FilmListViewModel, onCarSelected: (Film) -> Unit) {

    val films = viewModel.films.collectAsState().value
    val userData = viewModel.userData.collectAsState().value

    LazyColumn(modifier = Modifier.padding(bottom = 15.dp)) {
        items(films) { car ->
            CarTile(onlyFavourites = onlyFavourites, film = car, userData = userData, onCarSelected)
        }
    }
}