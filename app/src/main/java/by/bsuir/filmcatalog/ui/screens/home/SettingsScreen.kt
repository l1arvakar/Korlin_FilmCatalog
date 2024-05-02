package by.bsuir.filmcatalog.ui.screens.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import by.bsuir.filmcatalog.R
import by.bsuir.filmcatalog.navigation.Routes
import by.bsuir.filmcatalog.navigation.Routes.HOME
import by.bsuir.filmcatalog.navigation.Routes.SIGN_IN
import by.bsuir.filmcatalog.services.AuthenticationService
import by.bsuir.filmcatalog.ui.theme.accentColor
import by.bsuir.filmcatalog.ui.theme.backgroundColor
import by.bsuir.filmcatalog.ui.theme.primaryColor
import by.bsuir.filmcatalog.ui.theme.textColor
import by.bsuir.filmcatalog.viewmodel.SettingsViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat", "UnrememberedMutableState")
@Composable
fun SettingsScreen(viewModel: SettingsViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val sdf = SimpleDateFormat("dd.MM.yyyy")
    val openDialog = remember { mutableStateOf(false) }

    val userData = viewModel.userData.collectAsState().value

    val filmCountries = listOf(
        "US",
        "DE",
        "GB",
        "FR",
        "IT",
        "ES",
        "CZ",
        "SE",
        "RU",
        "CN",
        "JP",
        "KR"
    )
    val carBodyTypes = listOf(
        "",
        "Седан",
        "Хэтчбек",
        "Универсал",
        "Купе",
        "Кабриолет",
        "Внедорожник",
        "Кроссовер",
        "Минивэн",
        "Пикап",
        "Лифтбек",
        "Лимузин",
        "Фургон"
    )
    val carDriverTypes = listOf("", "FWD", "RWD", "AWD", "4WD", "EV")
    val transmissionTypes = listOf(
        "",
        "Механическая",
        "Автоматическая",
        "Роботизированная",
        "Вариатор",
        "Полуавтоматическая",
        "Двухсцепной робот"
    )
    val genders = listOf("Мужчина", "Женщина")

    if (userData != null) {

        var expandedCarBody by remember { mutableStateOf(false) }
        var expandedCarDrive by remember { mutableStateOf(false) }
        var expandedCarTransmission by remember { mutableStateOf(false) }

        var firstname by remember { mutableStateOf(userData.firstname ?: "") }
        var lastname by remember { mutableStateOf(userData.lastname ?: "") }
        var description by remember { mutableStateOf(userData.description ?: "") }
        var address by remember { mutableStateOf(userData.address ?: "") }
        var phone by remember { mutableStateOf(userData.phone ?: "") }
        var birthday by remember { mutableStateOf(userData.birthday ?: Timestamp.now()) }
        var filmCountry by remember { mutableStateOf(userData.filmCountry ?: "") }
        var favActor by remember { mutableStateOf(userData.favActor ?: "") }
        var favDirector by remember { mutableStateOf(userData.favDirector ?: "") }
        var gender by remember { mutableStateOf(userData.gender ?: genders[0]) }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                SettingsTopBar(
                    "Профиль",
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
                                    AuthenticationService().signOut()
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
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = backgroundColor
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Данные пользователя",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                    )
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = firstname,
                                    onValueChange = { firstname = it },
                                    label = { Text("Имя") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = lastname,
                                    onValueChange = { lastname = it },
                                    label = { Text("Фамилия") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text("Описание") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Дата рождения: ",
                                        fontSize = 17.sp
                                    )
                                    TextButton(
                                        onClick = {
                                            selectDate(
                                                context,
                                                LocalDate.now(),
                                                onDateSelected = { year, month, day ->
                                                    val calendar = Calendar.getInstance()
                                                    calendar.set(year, month - 1, day, 0, 0);
                                                    birthday = Timestamp(calendar.time)
                                                })
                                        }
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = sdf.format(birthday.toDate()),
                                                fontSize = 17.sp,
                                                color = primaryColor
                                            )
                                            Icon(
                                                imageVector = Icons.Default.DateRange,
                                                contentDescription = "Выбрать дату",
                                                tint = primaryColor
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = address,
                                    onValueChange = { address = it },
                                    label = { Text("Адрес") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = phone,
                                    onValueChange = { phone = it },
                                    label = { Text("Номер телефона") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Пол:",
                                    style = TextStyle(
                                        fontSize = 17.sp,
                                    )
                                )

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { gender = genders[0] }) {
                                        RadioButton(
                                            selected = genders[0] == gender,
                                            onClick = { gender = genders[0] }
                                        )
                                        Text(text = genders[0], color = textColor, fontSize = 17.sp)
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable { gender = genders[1] }) {
                                        RadioButton(
                                            selected = genders[1] == gender,
                                            onClick = { gender = genders[1] }
                                        )
                                        Text(text = genders[1], color = textColor, fontSize = 17.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Любимое",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                    )
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Страна:",
                                        color = textColor,
                                        fontSize = 16.sp,
                                        modifier = Modifier.width(120.dp)
                                    )

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .clickable { openDialog.value = true }
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(width = 30.dp, height = 30.dp)
                                        ) {
                                            CountryFlagImage(country = filmCountry)
                                        }
                                        Spacer(Modifier.width(10.dp))
                                        Text(
                                            text = Locale("", filmCountry).getDisplayCountry(
                                                Locale(
                                                    filmCountry
                                                )
                                            ),
                                            fontSize = 17.sp,
                                        )
                                    }

                                    when {
                                        openDialog.value -> {
                                            DialogPickCountries(
                                                onDismissRequest = {
                                                    openDialog.value = false
                                                    if(it != null)
                                                        filmCountry = it
                                                },
                                                countries = filmCountries
                                            )
                                        }
                                    }

                                }

                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = favActor,
                                    onValueChange = { favActor = it },
                                    label = { Text("Актёр") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))
                                OutlinedTextField(
                                    value = favDirector,
                                    onValueChange = { favDirector = it },
                                    label = { Text("Режиссёр") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    ElevatedButton(
                                        onClick = {
                                            if (true) {
                                                viewModel.updateUserData(
                                                    firstname = firstname,
                                                    lastname = lastname,
                                                    description = description,
                                                    birthday = birthday,
                                                    gender = gender,
                                                    address = address,
                                                    filmCountry = filmCountry,
                                                    phone = phone,
                                                    favActor = favActor,
                                                    favDirector = favDirector,
                                                )
                                                navController.navigate(HOME)
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = primaryColor,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = "Обновить")
                                    }

                                    ElevatedButton(
                                        onClick = {
                                            viewModel.deleteAccount { navController.navigate(SIGN_IN) }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentColor,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text(text = "Удалить аккаунт")
                                    }
                                }
                            }
                        }
                    }
                }
            })
    }
}

fun selectDate(
    context: Context,
    currentDate: LocalDate,
    onDateSelected: (year: Int, month: Int, dayOfMonth: Int) -> Unit
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected(year, month + 1, dayOfMonth)
        },
        currentDate.year,
        currentDate.monthValue - 1,
        currentDate.dayOfMonth
    ).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(title: String, onMenuItemClick: (MenuItem) -> Unit) {
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
                    onMenuItemClick(MenuItem.Home)
                    showMenu = false
                },
                    text = {
                        Row {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Список",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.padding(3.dp, 0.dp))
                            Text(
                                "Список", style = TextStyle(
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.secondary,
                                )
                            )
                        }
                    })
                DropdownMenuItem(onClick = {
                    onMenuItemClick(MenuItem.Favourites)
                    showMenu = false
                },
                    text = {
                        Row {
                            Icon(
                                Icons.Default.Favorite,
                                tint = MaterialTheme.colorScheme.secondary,
                                contentDescription = "Избранное",
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            Spacer(modifier = Modifier.padding(3.dp, 0.dp))
                            Text(
                                "Избранное", style = TextStyle(
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
fun DialogPickCountries(
    onDismissRequest: (country: String?) -> Unit,
    countries: List<String>
) {
    Dialog(onDismissRequest = { onDismissRequest(null) }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                countries.forEach { country ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 3.dp)
                            .height(40.dp)
                            .clickable { onDismissRequest(country) }
                    ) {
                        Spacer(Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 30.dp, height = 30.dp)
                        ) {
                            CountryFlagImage(country = country)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = Locale("", country).getDisplayCountry(Locale(country)),
                            fontSize = 17.sp,
                        )
                    }
                }
            }
        }
    }
}