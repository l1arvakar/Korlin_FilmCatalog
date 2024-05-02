package by.bsuir.filmcatalog

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import by.bsuir.filmcatalog.navigation.Routes.FAVOURITES
import by.bsuir.filmcatalog.navigation.Routes.HOME
import by.bsuir.filmcatalog.navigation.Routes.SETTINGS
import by.bsuir.filmcatalog.navigation.Routes.SIGN_IN
import by.bsuir.filmcatalog.navigation.Routes.SIGN_UP
import by.bsuir.filmcatalog.services.AuthenticationService
import by.bsuir.filmcatalog.ui.screens.authentication.SignInScreen
import by.bsuir.filmcatalog.ui.screens.authentication.SignUpScreen
import by.bsuir.filmcatalog.ui.screens.home.HomeScreen
import by.bsuir.filmcatalog.ui.screens.home.SettingsScreen
import by.bsuir.filmcatalog.ui.theme.CarAppTheme
import by.bsuir.filmcatalog.viewmodel.SettingsViewModel
import by.bsuir.filmcatalog.viewmodel.SignInViewModel

class MainActivity : ComponentActivity() {

    private val authenticationService by lazy {
        AuthenticationService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = SIGN_IN) {
                        composable(SIGN_IN) {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (authenticationService.getSignedInUser() != null) {
                                    navController.navigate(HOME)
                                }
                            }

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(HOME)
                                    viewModel.resetState()
                                }
                            }

                            SignInScreen(
                                state = state,
                                auth = authenticationService,
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        composable(SIGN_UP) {
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            LaunchedEffect(key1 = Unit) {
                                if (authenticationService.getSignedInUser() != null) {
                                    navController.navigate(HOME)
                                }
                            }

                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate(HOME)
                                    viewModel.resetState()
                                }
                            }

                            SignUpScreen(
                                state = state,
                                auth = authenticationService,
                                viewModel = viewModel,
                                navController = navController
                            )
                        }
                        composable(HOME) {
                            HomeScreen(
                                isFav = false,
                                auth = authenticationService,
                                navController = navController
                            )
                        }
                        composable(FAVOURITES) {
                            HomeScreen(
                                isFav = true,
                                auth = authenticationService,
                                navController = navController
                            )
                        }
                        composable(SETTINGS){
                            val viewModel = viewModel<SettingsViewModel>()
                            SettingsScreen(viewModel = viewModel, navController = navController)
                        }
                    }
                }
            }
        }
    }
}