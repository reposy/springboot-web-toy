package springbootkotlin.toy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.user.login.repository.UserRepository

@SpringBootTest
class H2DBConnectionTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `test`() {
        // Given
        val user = User(name = "Test User", email = "test@example.com")

        // When
        val savedUser = userRepository.save(user)
        println(savedUser)
        val foundUser = userRepository.findById(savedUser.id)

        // Then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().name).isEqualTo("Test User")
    }
}