package by.bsuir.filmcatalog.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import by.bsuir.filmcatalog.data.*
import by.bsuir.filmcatalog.services.DatabaseService
import by.bsuir.filmcatalog.viewmodel.PreviewImageViewModel


@Composable
fun CarTile(onlyFavourites: Boolean, film: Film, userData: UserData?, onCarSelected: (Film) -> Unit) {
    film.isFav = userData?.favFilms?.contains(film.uid) ?: false

    if (onlyFavourites && !film.isFav) return

    Card(
        modifier = Modifier
            .padding(20.dp, 15.dp, 20.dp, 0.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
    ) {
        ListItem(
            modifier = Modifier.clickable { onCarSelected(film) },
            headlineContent = { film.name?.let { Text(text = it) } },
            supportingContent = { film.year?.let { Text(text = it) } },
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .height(80.dp)
                        .width(100.dp)
                ) {

                    PreviewImage(
                        viewModel = PreviewImageViewModel(),
                        path = "${film.name}/Poster.jpg",
                    )
                }
            },
            trailingContent = {
                Column(
                    Modifier.height(80.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        userData?.let {
                            val favCars = it.favFilms.toMutableList()
                            if (film.isFav) {
                                favCars.remove(film.uid)
                            } else {
                                film.uid?.let { it1 -> favCars.add(it1) }
                            }

                            DatabaseService(userData.uid).updateUserFavCars(favCars)
                        }
                    }) {
                        Icon(
                            imageVector = if (film.isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                        )
                    }
                }
            }
        )
    }
}


//ListItem(
//modifier = Modifier.clickable { onCarSelected(car) },
//headlineContent = { car.name?.let { Text(text = it) } },
//supportingContent = { car.years?.let { Text(text = it) } },
//leadingContent = {
//    Box(
//        modifier = Modifier
//            .fillMaxHeight()
//            .width(100.dp)
//            .fillMaxHeight(),
//    ) {
//        Box(modifier = Modifier
//            .width(100.dp)
//            .height(50.dp).align(Alignment.Center)) {
//            PreviewImage(
//                viewModel = PreviewImageViewModel(),
//                path = "${car.name}/preview.jpg",
//            )
//        }
//    }
//},
//trailingContent = {
//    Column(
//        Modifier.fillMaxHeight(),
//        verticalArrangement = Arrangement.Center
//    ) {
//        IconButton(onClick = {
//            userData?.let {
//                val favCars = it.favCars.toMutableList()
//                if (car.isFav) {
//                    favCars.remove(car.uid)
//                } else {
//                    car.uid?.let { it1 -> favCars.add(it1) }
//                }
//
//                DatabaseService(userData.uid).updateUserFavCars(favCars)
//            }
//        }) {
//            Icon(
//                imageVector = if (car.isFav) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
//                contentDescription = null,
//            )
//        }
//    }
//}
//)