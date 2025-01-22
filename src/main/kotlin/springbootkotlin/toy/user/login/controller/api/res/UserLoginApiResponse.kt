package springbootkotlin.toy.user.login.controller.api.res

data class UserLoginApiResponse(
    val message: String,
    val token: String = ""
)