package springbootkotlin.toy.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import springbootkotlin.toy.entity.QUser
import springbootkotlin.toy.entity.User

@SpringBootTest
class QuerydslConfigTest {

    @Autowired
    private lateinit var jpaQueryFactory: JPAQueryFactory

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Test
    fun `JPAQueryFactory should be created successfully`() {
        // Then
        assertThat(jpaQueryFactory).isNotNull
    }

    @Test
    fun `EntityManager should be available`() {
        // Then
        assertThat(entityManager).isNotNull
    }

    @Test
    @Transactional
    fun `JPAQueryFactory should interact with EntityManager`() {
        // Given
        val user = User(
            userId = "testUser",
            encryptedPassword = "password",
            name = "Test User",
            email = "testuser@example.com"
        )
        entityManager.persist(user)
        entityManager.flush()

        // When
        val qUser = QUser.user
        val fetchedUser = jpaQueryFactory.selectFrom(qUser)
            .where(qUser.userId.eq("testUser"))
            .fetchOne()

        // Then
        assertThat(fetchedUser).isNotNull
        assertThat(fetchedUser?.userId).isEqualTo("testUser")
    }
}