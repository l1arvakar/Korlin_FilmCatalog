package by.bsuir.filmcatalog.data

import com.google.firebase.Timestamp

data class UserData(
    var uid:String? = null,
    val firstname:String? = null,
    val lastname:String? = null,
    val birthday: Timestamp? = null,
    val description:String? = null,
    val phone:String? = null,
    val gender:String? = null,
    val address:String? = null,
    val filmCountry:String? = null,
    val favActor:String? = null,
    val favDirector:String? = null,
    var favFilms:List<String> = arrayListOf()
)
