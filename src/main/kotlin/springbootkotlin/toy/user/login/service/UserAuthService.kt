package springbootkotlin.toy.user.login.service

import springbootkotlin.toy.user.login.dto.UserDTO

interface UserAuthService {
    fun findUser(userId: String, password: String): UserDTO
    fun generateUserJwt(userId: String, password: String): String
}