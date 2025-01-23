package springbootkotlin.toy.users.auth.api.req

data class UserAuthApiLoginRequest(
    val userId: String,
    val password: String
)
