package by.bsuir.filmcatalog.data

data class Film(
    var uid: String? = null,
    val name: String? = null,
    val year: String? = null,
    val description: String? = null,
    val country: String? = null,
    val genre: String? = null,
    val director: String? = null,

    var isFav: Boolean = false
)
