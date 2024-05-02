package by.bsuir.filmcatalog.states

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)