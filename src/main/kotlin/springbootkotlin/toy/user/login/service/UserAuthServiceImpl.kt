package springbootkotlin.toy.user.login.service

import org.springframework.stereotype.Service
import springbootkotlin.toy.user.login.repository.UserRepository
import springbootkotlin.toy.user.login.dto.UserDTO
import springbootkotlin.toy.util.jwt.JwtUtil

@Service
class UserAuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtUtil: JwtUtil
) : UserAuthService {

    override fun findUser(userId: String, password: String): UserDTO {
        val user = userRepository.findByUserIdAndPassword(userId, password)
            ?: throw IllegalArgumentException("Invalid username or password")

        return UserDTO(
            id = user.id,
            userId = user.userId,
            email = user.email
        )
    }

    override fun generateUserJwt(userId: String, password: String): String {
        val user = findUser(userId, password) // Reuse authentication logic

        val subject = "USER${user.id}"
        val claims = mapOf(
            "userId" to user.userId
        )

        return jwtUtil.generateToken(subject, claims)
    }

}