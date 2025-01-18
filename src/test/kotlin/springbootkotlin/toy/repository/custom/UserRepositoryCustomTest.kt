package springbootkotlin.toy.repository.custom

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import springbootkotlin.toy.entity.User
import springbootkotlin.toy.repository.UserRepository
import springbootkotlin.toy.repository.custom.impl.UserRepositoryCustomImpl

@SpringBootTest
@Transactional
class UserRepositoryCustomTest {

    @Autowired
    private lateinit var userRepositoryCustom: UserRepositoryCustomImpl

    @Autowired
    private lateinit var userRepository: UserRepository // 초기 데이터 삽입용

    @BeforeEach
    fun setUp() {
        // 데이터 초기화
        userRepository.deleteAll()
        userRepository.save(User(name = "Alice", email = "alice@example.com"))
        userRepository.save(User(name = "Bob", email = "bob@example.com"))
        userRepository.save(User(name = "Alice", email = "alice2@example.com"))
    }

    @Test
    fun `특정 이름과 이메일로 사용자 조회`() {
        // Given
        val name = "Alice"
        val email = "alice@example.com"

        // When
        val result = userRepositoryCustom.findSpecialUsers(name, email)

        // Then
        assertThat(result).hasSize(1) // 하나의 사용자만 조회되어야 함
        val user = result[0]
        assertThat(user.name).isEqualTo(name)
        assertThat(user.email).isEqualTo(email)
    }

    @Test
    fun `이름과 이메일이 일치하지 않는 경우 빈 리스트 반환`() {
        // Given
        val name = "Nonexistent"
        val email = "noemail@example.com"

        // When
        val result = userRepositoryCustom.findSpecialUsers(name, email)

        // Then
        assertThat(result).isEmpty() // 조건에 맞는 사용자가 없어야 함
    }

    @Test
    fun `동일한 이름을 가진 사용자 중 특정 이메일로 조회`() {
        // Given
        val name = "Alice"
        val email = "alice2@example.com"

        // When
        val result = userRepositoryCustom.findSpecialUsers(name, email)

        // Then
        assertThat(result).hasSize(1) // 조건에 맞는 사용자가 하나 있어야 함
        val user = result[0]
        assertThat(user.name).isEqualTo(name)
        assertThat(user.email).isEqualTo(email)
    }
}