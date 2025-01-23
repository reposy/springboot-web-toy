package springbootkotlin.toy.users.auth.service

import org.springframework.stereotype.Service
import springbootkotlin.toy.users.auth.repository.UserRepository
import springbootkotlin.toy.users.auth.dto.UserDTO
import springbootkotlin.toy.util.JwtUtil

@Service
class UsersAuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) : UsersAuthService {

    override fun findUser(userId: String, password: String): UserDTO {
        val user = userRepository.findByUserIdAndPassword(userId, password)
            ?: throw IllegalArgumentException("Invalid username or password")

        return UserDTO(
            id = user.id,
            userId = user.userId,
            email = user.email,
            role = user.role,
            authority = user.authority
        )
    }

    override fun generateUserJwt(userId: String, password: String): String {
        val user = findUser(userId, password) // Reuse authentication logic

        val subject = "USER_NORMAL:${user.id}"
        val claims = mapOf(
            "userId" to user.userId
        )
        return jwtUtil.generateToken(subject, claims)
    }

}