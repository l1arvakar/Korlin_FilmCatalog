package by.bsuir.filmcatalog.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import by.bsuir.filmcatalog.R
import by.bsuir.filmcatalog.viewmodel.PreviewImageViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun PreviewImage(viewModel: PreviewImageViewModel, path: String) {
    val imageUrl by viewModel.getFileUrl(path).observeAsState()

    Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()){
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .build()
                ),
                contentDescription = null,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
            )
        }
    }
}