package springbootkotlin.toy.users.auth.service

import springbootkotlin.toy.users.auth.dto.UserDTO

interface UsersAuthService {
    fun findUser(userId: String, password: String): UserDTO
    fun generateUserJwt(userId: String, password: String): String
}