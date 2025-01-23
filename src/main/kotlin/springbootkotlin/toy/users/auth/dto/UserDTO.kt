package springbootkotlin.toy.users.auth.dto

data class UserDTO (
    val id: Long,
    val userId: String,
    val email: String,
    val role: String,
    val authority: String
)