package by.bsuir.filmcatalog.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.bsuir.filmcatalog.R
import by.bsuir.filmcatalog.data.Film
import by.bsuir.filmcatalog.ui.theme.textColor
import coil.compose.AsyncImage
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun FilmInfo(film: Film, folderPath: String) {

    Column(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .padding(bottom = 20.dp)
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        film.name?.let {
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
            )
        }
        film.year?.let {
            Text(
                text = it,
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 17.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        CarouselSlider(folderPath)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Описание",
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        )
        film.description?.let {
            Text(
                text = it,
                style = TextStyle(
                    fontSize = 17.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Страна",
                fontSize = 17.sp,
                color = textColor
            )
            film.country?.let {
                Box (modifier = Modifier
                    .size(width = 50.dp, height = 50.dp)) {
                    CountryFlagImage(it)
                }
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Жанр",
                fontSize = 17.sp,
                color = textColor
            )
            film.genre?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.End,
                    fontSize = 17.sp,
                    color = textColor
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Режиссёр",
                fontSize = 17.sp,
                color = textColor
            )
            film.director?.let {
                Text(
                    text = it,
                    textAlign = TextAlign.End,
                    fontSize = 17.sp,
                    color = textColor
                )
            }
        }
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun CountryFlagImage(country: String) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(
        "flag_${country.lowercase()}",
        "drawable",
        context.packageName
    )

    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = "Flag of $country",
            modifier = Modifier.fillMaxSize()
        )
    }
}

fun getDetailedFilesUrl(firebasePath: String, onResult: (List<String>) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = mutableListOf<String>()
            val storageRef = Firebase.storage.reference
            val listResult = storageRef.child(firebasePath).listAll().await()

            for (file in listResult.items) {
                val url = file.downloadUrl.await()
                result.add(url.toString())
            }

            onResult(result)
        } catch (e: Exception) {
            println("Failed with error '${e.message}'")
            onResult(emptyList())
        }
    }
}

@Composable
private fun CarouselSlider(folderPath: String) {
    val coroutineScope = rememberCoroutineScope()

    var imageUrls by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(key1 = folderPath) {
        coroutineScope.launch {
            getDetailedFilesUrl(folderPath) { urls ->
                imageUrls = urls
            }
        }
    }

    Column(
        modifier = Modifier
            .height(150.dp)
            .fillMaxWidth()
    ) {
        if (imageUrls.isNotEmpty()) {
            val listState = rememberLazyListState(initialFirstVisibleItemIndex = 3)

            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                itemsIndexed(imageUrls) { _, image ->
                    AsyncImage(
                        model = image,
                        contentDescription = "Image",
                        modifier = Modifier
                            .height(150.dp),

                        contentScale = ContentScale.FillHeight
                    )
                }
            }
        } else {
            Image(
                painter = painterResource(R.drawable.app_logo),
                contentDescription = "Placeholder",
                modifier = Modifier.height(150.dp)
            )
        }
    }
}