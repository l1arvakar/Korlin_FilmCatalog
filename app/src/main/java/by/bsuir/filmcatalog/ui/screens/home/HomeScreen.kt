package by.bsuir.filmcatalog.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import by.bsuir.filmcatalog.R
import by.bsuir.filmcatalog.data.Film
import by.bsuir.filmcatalog.navigation.Routes
import by.bsuir.filmcatalog.services.AuthenticationService
import by.bsuir.filmcatalog.ui.components.FilmList
import by.bsuir.filmcatalog.viewmodel.FilmListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(isFav: Boolean, auth: AuthenticationService, navController: NavHostController) {
    val (selectedCar, setSelectedCar) = remember { mutableStateOf<Film?>(null) }

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            HomeTopBar(
                isFav,
                if (!isFav) "Каталог" else "Избранное",
                onMenuItemClick = { menuItem ->
                    when (menuItem) {
                        MenuItem.Home -> {
                            navController.navigate(Routes.HOME)
                        }

                        MenuItem.Favourites -> {
                            navController.navigate(Routes.FAVOURITES)
                        }

                        MenuItem.Settings -> {
                            navController.navigate(Routes.SETTINGS)
                        }

                        MenuItem.Logout -> {
                            scope.launch {
                                auth.signOut()
                                Toast.makeText(
                                    context,
                                    "Signed out",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate(Routes.SIGN_IN)
                            }
                        }
                    }
                })
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                FilmList(
                    onlyFavourites = isFav,
                    viewModel = FilmListViewModel(),
                    onCarSelected = { car ->
                        setSelectedCar(car)
                        scope.launch {
                            showBottomSheet = true
                        }
                    })
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    BottomSheetContent(film = selectedCar)
                }
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(isFav: Boolean, title: String, onMenuItemClick: (MenuItem) -> Unit) {
    TopAppBar(
        navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "App Logo",
                modifier = Modifier.height(40.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.secondary,
        ),
        title = {
            Text(
                title, style = TextStyle(
                    fontSize = 20.sp
                ), modifier = Modifier.padding(20.dp, 0.dp)
            )
        },
        actions = {
            var showMenu by remember { mutableStateOf(false) }

            Button(onClick = { showMenu = true }) {
                Icon(
                    Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "Меню"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    "Меню", style = TextStyle(
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                )
            }

            DropdownMenu(
                modifier = Modifier.background(MaterialTheme.colorScheme.primary),
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
            ) {
                DropdownMenuItem(onClick = {
                    onMenuItemClick(if (isFav) MenuItem.Home else MenuItem.Favourites)
                    showMenu = false
                },
                    text = {
                        Row {
                            Icon(
                                if(isFav) Icons.AutoMirrored.Filled.List else Icons.Default.Favorite,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = if(isFav) "Список" else "Избранное",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.padding(3.dp, 0.dp))
                            Text(
                                if(isFav) "Список" else "Избранное", style = TextStyle(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            )
                        }
                    })
                DropdownMenuItem(onClick = {
                    onMenuItemClick(MenuItem.Settings)
                    showMenu = false
                },
                    text = {
                        Row {
                            Icon(
                                Icons.Default.Settings,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Профиль",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.padding(3.dp, 0.dp))
                            Text(
                                "Профиль", style = TextStyle(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            )
                        }
                    })
                DropdownMenuItem(onClick = {
                    onMenuItemClick(MenuItem.Logout)
                    showMenu = false
                },
                    text = {
                        Row {
                            Icon(
                                Icons.AutoMirrored.Default.ExitToApp,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Выйти",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.padding(3.dp, 0.dp))
                            Text(
                                "Выйти", style = TextStyle(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            )
                        }
                    })
            }
        }
    )
}

@Composable
fun BottomSheetContent(film: Film?) {
    film?.let {
        FilmInfo(film = it, folderPath = "${it.name}/screenshots")
    }
}