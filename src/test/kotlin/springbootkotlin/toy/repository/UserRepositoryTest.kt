package springbootkotlin.toy.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import springbootkotlin.toy.entity.User

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        userRepository.save(User(name = "Alice", email = "alice@example.com"))
        userRepository.save(User(name = "Bob", email = "bob@example.com"))
    }

    @Test
    fun `ID로 사용자 조회`() {
        // Given
        val user = userRepository.save(User(name = "Charlie", email = "charlie@example.com"))

        // When
        val foundUser = userRepository.findById(user.id).orElse(null)

        // Then
        assertThat(foundUser).isNotNull
        assertThat(foundUser.name).isEqualTo("Charlie")
        assertThat(foundUser.email).isEqualTo("charlie@example.com")
    }

    @Test
    fun `모든 사용자 조회`() {
        // When
        val users = userRepository.findAll()

        // Then
        assertThat(users).hasSize(2)
        assertThat(users.map { it.name }).containsExactlyInAnyOrder("Alice", "Bob")
    }

    @Test
    fun `사용자 삭제`() {
        // Given
        val user = userRepository.save(User(name = "Diana", email = "diana@example.com"))

        // When
        userRepository.delete(user)

        // Then
        val foundUser = userRepository.findById(user.id).orElse(null)
        assertThat(foundUser).isNull()
    }
}