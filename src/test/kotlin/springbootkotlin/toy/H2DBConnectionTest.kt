package springbootkotlin.toy

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.users.auth.repository.UserRepository

@SpringBootTest
class H2DBConnectionTest {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        val testUser = User(
            userId = "testUser",
            encryptedPassword = "password123",
            name = "Test User",
            email = "test@example.com"
        )
        userRepository.save(testUser) // 테스트용 데이터 삽입
    }

    @AfterEach
    fun tearDown() {
        // "test_"로 시작하는 userId를 가진 데이터만 삭제
        userRepository.findAll().filter { it.userId.startsWith("test") }
            .forEach { userRepository.delete(it) }
    }

    @Test
    fun `should save and find user`() {
        // Given
        val user = User(
            userId = "testTester",
            encryptedPassword = "testtest",
            name = "Tester User",
            email = "tester@example.com"
        )

        // When
        val savedUser = userRepository.save(user) // 유저 저장
        val foundUser = userRepository.findById(savedUser.id) // 저장된 유저 조회

        // Then
        assertThat(foundUser).isPresent
        assertThat(foundUser.get().name).isEqualTo("Tester User")
        assertThat(foundUser.get().email).isEqualTo("tester@example.com")
    }

    @Test
    fun `should find user by userId and password`() {
        // When
        val foundUser = userRepository.findByUserIdAndPassword("testUser", "password123")

        // Then
        assertThat(foundUser).isNotNull
        assertThat(foundUser?.name).isEqualTo("Test User")
        assertThat(foundUser?.email).isEqualTo("test@example.com")
    }
}