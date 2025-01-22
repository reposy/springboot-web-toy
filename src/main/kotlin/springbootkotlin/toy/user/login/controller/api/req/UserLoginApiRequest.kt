package springbootkotlin.toy.user.login.controller.api.req

data class UserLoginApiRequest(
    val userId: String,
    val password: String
)
