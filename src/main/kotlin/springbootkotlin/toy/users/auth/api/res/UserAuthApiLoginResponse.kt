package springbootkotlin.toy.users.auth.api.res

data class UserAuthApiLoginResponse(
    val message: String,
    val token: String = ""
)